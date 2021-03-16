package di.container;

public class BeanPropertyWithValue implements BeanProperty {
  private String id;

  private Object value;

  private Class<?> clazz;

  private String name;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getBean() {
    return value;
  }

  @Override
  public Class<?> getClazz() {
    return clazz;
  }

  public BeanPropertyWithValue(String name, Object value, Class<?> clazz) {
    this.name = name;
    this.value = value;
    this.clazz = clazz;
  }


}
