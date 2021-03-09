package di.container;

public class BeanProperty {
  private String name;

  private String value;

  private Class<?> clazz;

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public Class<?> getClazz() {
    return clazz;
  }
}
