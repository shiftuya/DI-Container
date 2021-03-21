package di.beanparser;

import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.DIContainerException;
import di.container.annotations.Bean;
import di.container.dependency.Dependency;
import di.container.dependency.DependencyWithId;
import di.container.dependency.DependencyWithType;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
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
                for (Constructor<?> constructor : clazz.getConstructors()) {
                    Inject injectAnnotation = constructor.getAnnotation(Inject.class);
                    if (injectAnnotation == null) {
                        continue;
                    }

                    if (injectedConstructor != null) {
                        throw new DIContainerException("Multiple injected constructors");
                    }

                    injectedConstructor = constructor;
                }

                List<Dependency> constructorDependencies = new ArrayList<>();
                if (injectedConstructor != null) {
                    if (injectedConstructor.isVarArgs()) {
                        // todo VarArgs
                    } else {
                        for (Parameter parameter : injectedConstructor.getParameters()) {
                            Named namedAnnotation = parameter.getAnnotation(Named.class);
                            constructorDependencies.add(
                                namedAnnotation == null ?
                                    new DependencyWithType(beanFactory, parameter.getType()) :
                                    new DependencyWithId(beanFactory, namedAnnotation.value())
                            );
                        }
                    }
                }

                List<Dependency> setterDependencies = new ArrayList<>(); // todo setterDependencies

                List<Dependency> fieldDependencies = new ArrayList<>();
                for (Field field : clazz.getDeclaredFields()) {
                    Inject injectAnnotation = field.getAnnotation(Inject.class);
                    if (injectAnnotation == null) {
                        continue;
                    }

                    Named namedAnnotation = field.getAnnotation(Named.class);
                    fieldDependencies.add(
                        namedAnnotation == null ?
                            new DependencyWithType(beanFactory, field.getType()) :
                            new DependencyWithId(beanFactory, namedAnnotation.value())
                    );
                }

                BeanDescription beanDescription = new BeanDescription(
                    beanAnnotation.lifecycle(),
                    clazz,
                    false, // todo delete?
                    constructorDependencies,
                    setterDependencies,
                    fieldDependencies
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
}
