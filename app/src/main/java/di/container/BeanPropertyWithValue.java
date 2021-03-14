package di.container;

public class BeanPropertyWithValue implements BeanProperty {
  private String id;

  private Object value;

  private Class<?> clazz;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Object getBean() {
    return value;
  }

  @Override
  public Class<?> getClazz() {
    return clazz;
  }

  public BeanPropertyWithValue(String id, Object value, Class<?> clazz) {
    this.id = id;
    this.value = value;
    this.clazz = clazz;
  }
}
