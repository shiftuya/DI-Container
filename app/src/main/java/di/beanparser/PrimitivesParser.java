package di.beanparser;

public class PrimitivesParser {

    private final String value;
    private String className;

    public PrimitivesParser(String value) {
        this.value = value;
    }

    public PrimitivesParser(String value, String className) {
        this.value = value;
        this.className = className;
    }

    public TypedObject getTypedObject() throws BeanParserException { // todo change exception?
        return className != null ? parseValue(className, value) : parseValue(value);
    }

    private TypedObject parseValue(String className, String value) throws BeanParserException {
        try {
            return switch (className) {
                case "boolean" -> new TypedObject(boolean.class, Boolean.parseBoolean(value));
                case "byte" -> new TypedObject(byte.class, Byte.parseByte(value));
                case "char" -> new TypedObject(char.class, value.charAt(0));
                case "short" -> new TypedObject(short.class, Short.parseShort(value));
                case "int" -> new TypedObject(int.class, Integer.parseInt(value));
                case "long" -> new TypedObject(long.class, Long.parseLong(value));
                case "float" -> new TypedObject(float.class, Float.parseFloat(value));
                case "double" -> new TypedObject(double.class, Double.parseDouble(value));
                default -> new TypedObject(Class.forName(className), value);
            };
        } catch (NumberFormatException | IndexOutOfBoundsException | ClassNotFoundException e) {
            throw new BeanParserException(e);
        }
    }

    private TypedObject parseValue(String value) {
        try {
            return new TypedObject(byte.class, Byte.parseByte(value));
        } catch (NumberFormatException ignored) {}

        try {
            return new TypedObject(short.class, Short.parseShort(value));
        } catch (NumberFormatException ignored) {}

        try {
            return new TypedObject(int.class, Integer.parseInt(value));
        } catch (NumberFormatException ignored) {}

        try {
            return new TypedObject(long.class, Long.parseLong(value));
        } catch (NumberFormatException ignored) {}

        try {
            return new TypedObject(float.class, Float.parseFloat(value));
        } catch (NumberFormatException ignored) {}

        try {
            return new TypedObject(double.class, Double.parseDouble(value));
        } catch (NumberFormatException ignored) {}

        if (value.equalsIgnoreCase("true")) {
            return new TypedObject(boolean.class, true);
        }

        if (value.equalsIgnoreCase("false")) {
            return new TypedObject(boolean.class, false);
        }

        return new TypedObject(String.class, value);
    }
}
