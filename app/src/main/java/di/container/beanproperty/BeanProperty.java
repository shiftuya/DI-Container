package di.container.beanproperty;

import di.container.DIContainerException;

public interface BeanProperty { // todo -o why BeanProperty?
  String getFieldName();
  Object getBean() throws DIContainerException;
  Class<?> getClazz();
}
