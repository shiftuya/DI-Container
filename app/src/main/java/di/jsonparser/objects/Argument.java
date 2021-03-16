package di.jsonparser.objects;

public class Argument {

    private String ref;
    private String fieldName;
    private String type;
    private String value;
    private BeanJson bean;

    @Override
    public String toString() {
        return "Argument{" +
            "ref='" + ref + '\'' +
            ", fieldName='" + fieldName + '\'' +
            ", type='" + type + '\'' +
            ", value='" + value + '\'' +
            ", bean=" + bean +
            '}';
    }

    public String getRef() {
        return ref;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public BeanJson getBean() {
        return bean;
    }
}
