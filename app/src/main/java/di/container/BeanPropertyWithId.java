package di.container;

public class BeanPropertyWithId implements BeanProperty {
  private BeanFactory parentFactory;

  private String id;

  private Class<?> clazz;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Object getBean() throws DIContainerException {
    return parentFactory.getBean(id);
  }

  @Override
  public Class<?> getClazz() {
    return clazz;
  }

  public BeanPropertyWithId(BeanFactory parentFactory, String id, Class<?> clazz) {
    this.parentFactory = parentFactory;
    this.id = id;
    this.clazz = clazz;
  }
}
