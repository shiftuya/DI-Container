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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationBeanParser {

    private final BeanFactory beanFactory = new BeanFactory();

    public AnnotationBeanParser() throws ClassNotFoundException, IOException, URISyntaxException {
        this("");
    }

    public AnnotationBeanParser(String directory) throws IOException, URISyntaxException, ClassNotFoundException {
        Map<String, BeanDescription> beanMap = new HashMap<>();
        Set<BeanDescription> beanSet = new HashSet<>();

        for (String classFileName : getClassFileNames(directory)) { // "di/container"
            classFileName = classFileName
                .replaceAll(".class$", "")
                .replaceAll("\\\\", ".");

            System.out.println(classFileName);

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
                BeanLifecycle.SINGLETON, // todo
                clazz,
                false, // todo
                dependencies,
                new ArrayList<>() // todo
            ));
        }

        beanFactory.setBeanDescriptions(beanMap);
        beanFactory.setBeanDescriptionSet(beanSet);
    }

    private Set<String> getClassFileNames(String directory) throws IOException, URISyntaxException {
        Path directoryPath = Paths.get(directory);
        File codeSourceFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

        if (!codeSourceFile.isDirectory() && codeSourceFile.toString().endsWith(".jar")) {
            JarFile jarFile = new JarFile(codeSourceFile);

            Set<String> set = new HashSet<>();

            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                String jarEntryName = jarEntries.nextElement().getName();
                Path jarEntryPath = Paths.get(jarEntryName);

                if ((!directory.isEmpty() && !jarEntryPath.startsWith(directoryPath)) || !jarEntryName.endsWith(".class")) {
                    continue;
                }

                set.add(jarEntryPath.toString());
            }

            return set;
        } else {
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
}
