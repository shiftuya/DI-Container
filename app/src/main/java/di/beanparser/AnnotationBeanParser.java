package di.beanparser;

import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.BeanLifecycle;
import di.container.dependency.Dependency;
import di.container.dependency.DependencyWithType;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
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

    public AnnotationBeanParser() throws IOException, URISyntaxException {
        this("", new Class<?>[] {});
    }

    public AnnotationBeanParser(Class<?>... startupClasses) throws IOException, URISyntaxException {
        this("", startupClasses);
    }

    public AnnotationBeanParser(String directory) throws IOException, URISyntaxException {
        this(directory, new Class<?>[] {});
    }

    public AnnotationBeanParser(String directory, Class<?>... startupClasses) throws IOException, URISyntaxException {
        this.startupClasses = startupClasses;

        Map<String, BeanDescription> beanMap = new HashMap<>();
        Set<BeanDescription> beanSet = new HashSet<>();

        for (String classFileName : getClassFileNames(directory)) {
            classFileName = classFileName
                .replaceAll(".class$", "")
                .replaceAll("\\\\", ".");

            try {
                Class<?> clazz = Class.forName(classFileName);
                List<Dependency> dependencies = new ArrayList<>();
                for (Constructor<?> constructor : clazz.getConstructors()) {
                    Inject injectAnnotation = constructor.getAnnotation(Inject.class);
                    if (injectAnnotation == null) {
                        continue;
                    }

                    if (constructor.isVarArgs()) {
                        // todo
                    } else {
                        for (Class<?> dependencyClass : constructor.getParameterTypes()) {
                            dependencies.add(new DependencyWithType(beanFactory, dependencyClass));
                        }
                    }

                    break; // todo throw if multiple injected constructors
                }

                beanSet.add(new BeanDescription(
                    BeanLifecycle.SINGLETON,
                    clazz,
                    false, // todo
                    dependencies,
                    new ArrayList<>(), // todo
                    new ArrayList<>() // todo field args
                ));
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
