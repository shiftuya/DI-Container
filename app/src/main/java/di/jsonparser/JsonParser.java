package di.jsonparser;

import com.google.gson.Gson;
import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.BeanLifecycle;
import di.container.beanproperty.BeanProperty;
import di.container.beanproperty.BeanPropertyWithId;
import di.container.beanproperty.BeanPropertyWithValue;
import di.container.beanproperty.InnerBeanProperty;
import di.jsonparser.objects.Argument;
import di.jsonparser.objects.BeanJson;
import di.jsonparser.objects.BeansJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

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

    public BeanFactory getBeanFactory(String jsonFileName) throws IOException, ClassNotFoundException {
        String jsonString = new String(JsonParser.class.getResourceAsStream("/" + jsonFileName).readAllBytes());
        BeansJson beansJson = new Gson().fromJson(jsonString, BeansJson.class);

        Map<String, BeanDescription> beans = new HashMap<>();
        for (BeanJson beanJson : beansJson.getBeans()) {
            beans.put(beanJson.getId(), parseBeanDescription(beanJson));
        }

        beanFactory.setBeans(beans);

        return beanFactory;
    }

    private BeanDescription parseBeanDescription(BeanJson beanJson) throws ClassNotFoundException {
        Class<?> clazz = PRIMITIVES.get(beanJson.getClassName());
        if (clazz == null) {
            clazz = Class.forName(beanJson.getClassName());
        }

        return new BeanDescription(
            switch (beanJson.getLifecycle()) {
                default -> BeanLifecycle.SINGLETON;
                case "thread" -> BeanLifecycle.THREAD;
                case "prototype" -> BeanLifecycle.PROTOTYPE;
            },
            clazz,
            beanJson.isProxy(),
            parseBeanProperties(beanJson.getConstructorArguments()),
            parseBeanProperties(beanJson.getSetterArguments())
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
                } else {
                    Class<?> clazz = PRIMITIVES.get(argument.getType());
                    if (clazz == null) {
                        clazz = Class.forName(argument.getType());
                    }

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
}
