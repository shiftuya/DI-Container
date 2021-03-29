package di.container.dependency;

import di.container.BeanDescription;
import java.util.List;

public class DependencyWithValue implements Dependency {

  private final Object bean;
  private final Class<?> clazz;
  private String fieldName;

  public DependencyWithValue(String fieldName, Object bean, Class<?> clazz) {
    this.fieldName = fieldName;
    this.bean = bean;
    this.clazz = clazz;
  }

  public DependencyWithValue(Object bean, Class<?> clazz) {
    this.bean = bean;
    this.clazz = clazz;
  }

  @Override
  public String getFieldName() {
    return fieldName;
  }

  @Override
  public Object getBean() {
    return bean;
  }

  @Override
  public Class<?> getClazz() {
    return clazz;
  }

  @Override
  public List<BeanDescription> getCycle(List<BeanDescription> traversedDependencies) {
    return null;
  }
}
