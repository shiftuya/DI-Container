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

}
