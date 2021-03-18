package di.container.beanproperty;

import di.container.BeanDescription;
import di.container.DIContainerException;

public class InnerBeanProperty implements BeanProperty {

  private String fieldName;
  private final BeanDescription beanDescription;

  public InnerBeanProperty(String fieldName, BeanDescription beanDescription) {
    this.fieldName = fieldName;
    this.beanDescription = beanDescription;
  }

  public InnerBeanProperty(BeanDescription beanDescription) {
    this.beanDescription = beanDescription;
  }

  @Override
  public String getFieldName() {
    return fieldName;
  }

  @Override
  public Object getBean() throws DIContainerException {
    return beanDescription.getBean();
  }

  @Override
  public Class<?> getClazz() {
    return beanDescription.getClazz();
  }
}
