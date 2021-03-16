package di.container.beanproperty;

import di.container.BeanFactory;
import di.container.DIContainerException;

public class BeanPropertyWithId implements BeanProperty {
  private BeanFactory parentFactory;

  private String id;

  private Class<?> clazz; // todo delete

  private String name;

  @Override
  public String getFieldName() {
    return name;
  }

  @Override
  public Object getBean() throws DIContainerException {
    return parentFactory.getBean(id);
  }

  @Override
  public Class<?> getClazz() {
    return clazz;
  }

  public BeanPropertyWithId(BeanFactory parentFactory, String id) {
    this.parentFactory = parentFactory;
    this.id = id;
  }

  public BeanPropertyWithId(BeanFactory parentFactory, String id, String name) {
    this.parentFactory = parentFactory;
    this.id = id;
    this.name = name;
  }
}
