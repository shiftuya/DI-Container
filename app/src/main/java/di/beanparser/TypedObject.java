package di.beanparser;

public class TypedObject {

    private final Class<?> type;
    private final Object value;

    public TypedObject(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
