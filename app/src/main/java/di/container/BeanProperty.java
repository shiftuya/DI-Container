package di.container;

public class BeanProperty {
  private String name;

  // possible options: primitives, String, another bean
  private Object value;

  private Class<?> clazz;

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }
}
