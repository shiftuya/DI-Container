package di.beanparser;

import com.google.gson.Gson;
import di.beanparser.objects.Argument;
import di.beanparser.objects.Bean;
import di.beanparser.objects.Beans;
import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.BeanLifecycle;
import di.container.dependency.Dependency;
import di.container.dependency.DependencyWithId;
import di.container.dependency.DependencyWithType;
import di.container.dependency.DependencyWithValue;
import di.container.dependency.InjectableSetterMethod;
import di.container.dependency.InnerDependency;
import di.container.dependency.ProviderDependency;
import di.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonBeanParser implements BeanParser {

    private static final Map<String, Class<?>> PRIMITIVES = new HashMap<>() {{
        put("boolean", boolean.class);
        put("byte", byte.class);
        put("char", char.class);
        put("short", short.class);
        put("int", int.class);
        put("long", long.class);
        put("float", float.class);
        put("double", double.class);
    }};

    private final BeanFactory beanFactory = new BeanFactory();

    public JsonBeanParser(String jsonFileName) throws ClassNotFoundException, IOException {
        Beans beans = new Gson().fromJson(Utils.getResourceAsString(jsonFileName), Beans.class);

        Map<String, BeanDescription> beanMap = new HashMap<>();
        Set<BeanDescription> beanSet = new HashSet<>();
        for (Bean bean : beans.getBeans()) {
            BeanDescription beanDescription = parseBeanDescription(bean);
            if (bean.getId() != null) {
                beanMap.put(bean.getId(), beanDescription);
            } else {
                beanSet.add(beanDescription);
            }
        }

        beanFactory.setBeanDescriptions(beanMap);
        beanFactory.setBeanDescriptionSet(beanSet);
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    private BeanDescription parseBeanDescription(Bean bean) throws ClassNotFoundException {
        return new BeanDescription(
            switch (bean.getLifecycle()) {
                default -> BeanLifecycle.SINGLETON; // todo throw
                case "thread" -> BeanLifecycle.THREAD;
                case "prototype" -> BeanLifecycle.PROTOTYPE;
            },
            getClazz(bean.getClassName()),
            bean.isProxy(),
            parseBeanProperties(bean.getConstructorArguments()),
            parseBeanProperties(bean.getFields()),
            parseBeanProperties(bean.getSetterArguments()).stream().map(InjectableSetterMethod::new).collect(Collectors.toList())
        );
    }

    private List<Dependency> parseBeanProperties(Argument[] arguments) throws ClassNotFoundException {
        List<Dependency> dependencies = new ArrayList<>();

        if (arguments != null) {
            for (Argument argument : arguments) {
                Dependency dependency;

                if (argument.getBean() != null) {
                    dependency = argument.getFieldName() != null ?
                        new InnerDependency(argument.getFieldName(), parseBeanDescription(argument.getBean())) :
                        new InnerDependency(parseBeanDescription(argument.getBean()));
                } else if (argument.getRef() != null) {
                    dependency = argument.getFieldName() != null ?
                        new DependencyWithId(beanFactory, argument.getRef(), argument.getFieldName()) :
                        new DependencyWithId(beanFactory, argument.getRef());
                } else if (argument.getValue() != null) { // todo parse real type value
                    Class<?> clazz = getClazz(argument.getClassName());

                    dependency = argument.getFieldName() != null ?
                        new DependencyWithValue(argument.getFieldName(), argument.getValue(), clazz) :
                        new DependencyWithValue(argument.getValue(), clazz);
                } else {
                    Class<?> clazz = getClazz(argument.getClassName());

                    dependency = argument.getFieldName() != null ?
                        new DependencyWithType(beanFactory, argument.getFieldName(), clazz) :
                        new DependencyWithType(beanFactory, clazz);
                }

                if (argument.isProvider()) {
                    dependency = argument.getFieldName() != null ?
                        new ProviderDependency(dependency, argument.getFieldName()) :
                        new ProviderDependency(dependency);
                }

                dependencies.add(dependency);
            }
        }

        return dependencies;
    }

    private Class<?> getClazz(String className) throws ClassNotFoundException {
        Class<?> clazz = PRIMITIVES.get(className);
        if (clazz == null) {
            clazz = Class.forName(className);
        }

        return clazz;
    }
}
