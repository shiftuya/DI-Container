package di.container.dependency;

import di.container.beandescription.BeanDescription;
import di.container.DIContainerException;
import java.util.List;

public class InnerDependency implements Dependency {

  private String fieldName;
  private final BeanDescription beanDescription;

  public InnerDependency(String fieldName, BeanDescription beanDescription) {
    this.fieldName = fieldName;
    this.beanDescription = beanDescription;
  }

  public InnerDependency(BeanDescription beanDescription) {
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

  @Override
  public List<BeanDescription> getCycle(List<BeanDescription> traversedBeans)
      throws DIContainerException {
    return beanDescription.getCycle(traversedBeans);
  }
}
