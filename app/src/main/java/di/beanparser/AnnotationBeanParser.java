package di.beanparser;

import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.DIContainerException;
import di.container.annotations.Bean;
import di.container.dependency.Dependency;
import di.container.dependency.DependencyWithId;
import di.container.dependency.DependencyWithType;
import di.container.dependency.GenericInjectableMethod;
import di.container.dependency.InjectableMethod;
import di.container.dependency.ProviderDependency;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationBeanParser implements BeanParser {

    private final Class<?>[] startupClasses;
    private final BeanFactory beanFactory = new BeanFactory();

    public AnnotationBeanParser() throws IOException, URISyntaxException, DIContainerException {
        this("", new Class<?>[] {});
    }

    public AnnotationBeanParser(Class<?>... startupClasses) throws IOException, URISyntaxException, DIContainerException {
        this("", startupClasses);
    }

    public AnnotationBeanParser(String directory) throws IOException, URISyntaxException, DIContainerException {
        this(directory, new Class<?>[] {});
    }

    public AnnotationBeanParser(String directory, Class<?>... startupClasses) throws IOException, URISyntaxException, DIContainerException {
        this.startupClasses = startupClasses;

        Map<String, BeanDescription> beanMap = new HashMap<>();
        Set<BeanDescription> beanSet = new HashSet<>();

        for (String classFileName : getClassFileNames(directory)) {
            classFileName = classFileName
                .replaceAll(".class$", "")
                .replaceAll("\\\\", ".");

            try {
                Class<?> clazz = Class.forName(classFileName);

                Bean beanAnnotation = clazz.getAnnotation(Bean.class);
                if (beanAnnotation == null) {
                    continue;
                }

                Constructor<?> injectedConstructor = null;
                Constructor<?>[] constructors = clazz.getConstructors();

                if (constructors.length == 0) {
                    throw new DIContainerException(clazz.getName() + " has no public constructors");
                }

                if (constructors.length == 1) {
                    injectedConstructor = constructors[0];
                } else {
                    for (Constructor<?> constructor : constructors) {
                        Inject injectAnnotation = constructor.getAnnotation(Inject.class);
                        if (injectAnnotation == null) {
                            continue;
                        }

                        if (injectedConstructor != null) {
                            throw new DIContainerException(clazz.getName() + " has multiple injected constructors");
                        }

                        injectedConstructor = constructor;
                    }

                    if (injectedConstructor == null) {
                        throw new DIContainerException(clazz.getName() + " has multiple public constructors and none of them is injected");
                    }
                }

                List<Dependency> constructorDependencies = new ArrayList<>();
                if (injectedConstructor.isVarArgs()) {
                    // todo VarArgs
                } else {
                    constructorDependencies = getDependencies(injectedConstructor);
                }

                List<InjectableMethod> injectableMethods = new ArrayList<>();
                for (Method method : clazz.getDeclaredMethods()) {
                    Inject injectAnnotation = method.getAnnotation(Inject.class);
                    if (injectAnnotation == null) {
                        continue;
                    }

                    injectableMethods.add(new GenericInjectableMethod(method.getName(), getDependencies(method)));
                }

                List<Dependency> fieldDependencies = new ArrayList<>();
                for (Field field : clazz.getDeclaredFields()) {
                    Inject injectAnnotation = field.getAnnotation(Inject.class);
                    if (injectAnnotation == null) {
                        continue;
                    }

                    Dependency dependency;
                    if (Provider.class.isAssignableFrom(field.getType())) {
                        try {
                            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                            Class<?> actualType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                            Named namedAnnotation = field.getAnnotation(Named.class);
                            dependency = new ProviderDependency(
                                namedAnnotation == null ?
                                    new DependencyWithType(beanFactory, actualType) :
                                    new DependencyWithId(beanFactory, namedAnnotation.value()),
                                field.getName()
                            );
                        } catch (ClassCastException e) {
                            throw new DIContainerException(clazz.getName() + " has raw injected Provider field: " + field.getName());
                        }
                    } else {
                        Named namedAnnotation = field.getAnnotation(Named.class);
                        dependency = namedAnnotation == null ?
                            new DependencyWithType(beanFactory, field.getType()) :
                            new DependencyWithId(beanFactory, namedAnnotation.value());
                    }

                    fieldDependencies.add(dependency);
                }

                BeanDescription beanDescription = new BeanDescription(
                    beanAnnotation.lifecycle(),
                    clazz,
                    false, // todo delete?
                    constructorDependencies,
                    fieldDependencies,
                    injectableMethods
                );

                Named namedAnnotation = clazz.getAnnotation(Named.class);
                if (namedAnnotation == null) {
                    beanSet.add(beanDescription);
                } else {
                    beanMap.put(namedAnnotation.value(), beanDescription);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException: " + classFileName);
            }
        }

        beanFactory.setBeanDescriptions(beanMap);
        beanFactory.setBeanDescriptionSet(beanSet);
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    private Set<String> getClassFileNames(String directory) throws IOException, URISyntaxException {
        File codeSourceFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

        if (!codeSourceFile.isDirectory() && codeSourceFile.toString().endsWith(".jar")) {
            return getJarFileClassFileNames(directory, codeSourceFile);
        } else {
            Set<String> set = getDirectoryClassFileNames(directory, codeSourceFile);

            for (Class<?> clazz : startupClasses) {
                set.addAll(getDirectoryClassFileNames(directory, new File(
                    clazz.getProtectionDomain().getCodeSource().getLocation().toURI())));
            }

            return set;
        }
    }

    private Set<String> getJarFileClassFileNames(String directory, File codeSourceFile) throws IOException {
        Path directoryPath = Paths.get(directory);
        JarFile jarFile = new JarFile(codeSourceFile);

        Set<String> set = new HashSet<>();

        for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
            String jarEntryName = jarEntry.getName();
            Path jarEntryPath = Paths.get(jarEntryName);

            if ((!directory.isEmpty() && !jarEntryPath.startsWith(directoryPath)) || !jarEntryName.endsWith(".class")) {
                continue;
            }

            set.add(jarEntryPath.toString());
        }

        return set;
    }

    private Set<String> getDirectoryClassFileNames(String directory, File codeSourceFile) throws IOException {
        Path directoryPath = Paths.get(directory);
        Path codeSourcePath = codeSourceFile.toPath();
        try (Stream<Path> stream = Files.walk(codeSourcePath)) {
            return stream
                .filter(path -> !Files.isDirectory(path) && path.toString().endsWith(".class"))
                .map(codeSourcePath::relativize)
                .filter(path -> directory.isEmpty() || path.startsWith(directoryPath))
                .map(Path::toString)
                .collect(Collectors.toSet());
        }
    }

    private List<Dependency> getDependencies(Executable executable) {
        List<Dependency> dependencies = new ArrayList<>();

        for (Parameter parameter : executable.getParameters()) {
            Named namedAnnotation = parameter.getAnnotation(Named.class);
            dependencies.add(
                namedAnnotation == null ?
                    new DependencyWithType(beanFactory, parameter.getType()) :
                    new DependencyWithId(beanFactory, namedAnnotation.value())
            );
        }

        return dependencies;
    }
}
