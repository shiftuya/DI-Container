package di.beanparser;

import com.google.gson.Gson;
import di.beanparser.objects.Argument;
import di.beanparser.objects.Bean;
import di.beanparser.objects.Beans;
import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.BeanLifecycle;
import di.container.beanproperty.BeanProperty;
import di.container.beanproperty.BeanPropertyWithId;
import di.container.beanproperty.BeanPropertyWithValue;
import di.container.beanproperty.InnerBeanProperty;
import di.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBeanParser {

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
        for (Bean bean : beans.getBeans()) {
            beanMap.put(bean.getId(), parseBeanDescription(bean));
        }

        beanFactory.setBeans(beanMap);
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
            parseBeanProperties(bean.getSetterArguments())
        );
    }

    private List<BeanProperty> parseBeanProperties(Argument[] arguments) throws ClassNotFoundException {
        List<BeanProperty> beanProperties = new ArrayList<>();

        if (arguments != null) {
            for (Argument argument : arguments) {
                if (argument.getBean() != null) {
                    beanProperties.add(
                        argument.getFieldName() != null ?
                            new InnerBeanProperty(argument.getFieldName(), parseBeanDescription(argument.getBean())) :
                            new InnerBeanProperty(parseBeanDescription(argument.getBean()))
                    );
                } else if (argument.getRef() != null) {
                    beanProperties.add(
                        argument.getFieldName() != null ?
                            new BeanPropertyWithId(beanFactory, argument.getRef(), argument.getFieldName()) :
                            new BeanPropertyWithId(beanFactory, argument.getRef())
                    );
                } else { // todo parse real type value
                    Class<?> clazz = getClazz(argument.getClassName());

                    beanProperties.add(
                        argument.getFieldName() != null ?
                            new BeanPropertyWithValue(argument.getFieldName(), argument.getValue(), clazz) :
                            new BeanPropertyWithValue(argument.getValue(), clazz)
                    );
                }
            }
        }

        return beanProperties;
    }

    private Class<?> getClazz(String className) throws ClassNotFoundException {
        Class<?> clazz = PRIMITIVES.get(className);
        if (clazz == null) {
            clazz = Class.forName(className);
        }

        return clazz;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}