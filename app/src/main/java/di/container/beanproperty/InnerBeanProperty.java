package di.container.beanproperty;

import di.container.BeanDescription;
import di.container.DIContainerException;

public class InnerBeanProperty implements BeanProperty {
  private String fieldName;

  private BeanDescription description;

  private Class<?> clazz;

  @Override
  public String getFieldName() {
    return fieldName;
  }

  @Override
  public Object getBean() throws DIContainerException {
    return null;
  }

  @Override
  public Class<?> getClazz() {
    return clazz;
  }

  public InnerBeanProperty(String fieldName, BeanDescription description, Class<?> clazz) {
    this.fieldName = fieldName;
    this.description = description;
    this.clazz = clazz;
  }

  public InnerBeanProperty(BeanDescription description, Class<?> clazz) {
    this.description = description;
    this.clazz = clazz;
  }
}
