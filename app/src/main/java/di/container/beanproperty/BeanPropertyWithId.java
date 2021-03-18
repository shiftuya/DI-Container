package di.container.beanproperty;

import di.container.BeanFactory;
import di.container.DIContainerException;

public class BeanPropertyWithId implements BeanProperty {

  private final BeanFactory parentFactory; // todo -o why parent?
  private final String id;
  private Class<?> clazz; // todo -o delete
  private String fieldName;

  public BeanPropertyWithId(BeanFactory parentFactory, String id) {
    this.parentFactory = parentFactory;
    this.id = id;
  }

  public BeanPropertyWithId(BeanFactory parentFactory, String id, String fieldName) {
    this.parentFactory = parentFactory;
    this.id = id;
    this.fieldName = fieldName;
  }

  @Override
  public String getFieldName() {
    return fieldName;
  }

  @Override
  public Object getBean() throws DIContainerException {
    return parentFactory.getBean(id);
  }

  @Override
  public Class<?> getClazz() {
    return parentFactory.getBeanDescription(id).getClazz();
  }
}
