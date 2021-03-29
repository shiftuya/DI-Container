package di.beanparser.json.objects;

import java.util.Arrays;

public class Bean {

    private Profile profile;
    private String id;
    private String className;
    private String lifecycle = "singleton";
    private Argument[] constructorArguments;
    private Argument[] setterArguments;
    private Argument[] fields;

    @Override
    public String toString() {
        return "Bean{" +
            "profile=" + profile +
            ", id='" + id + '\'' +
            ", className='" + className + '\'' +
            ", lifecycle='" + lifecycle + '\'' +
            ", constructorArguments=" + Arrays.toString(constructorArguments) +
            ", setterArguments=" + Arrays.toString(setterArguments) +
            ", fields=" + Arrays.toString(fields) +
            '}';
    }

    public Profile getProfile() {
        return profile;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getLifecycle() {
        return lifecycle;
    }

    public Argument[] getConstructorArguments() {
        return constructorArguments;
    }

    public Argument[] getSetterArguments() {
        return setterArguments;
    }

    public Argument[] getFields() {
        return fields;
    }
}
