package di.container.dependency;

import di.container.BeanDescription;
import di.container.DIContainerException;
import java.util.List;

public interface Dependency {

  String getFieldName();

  Object getBean() throws DIContainerException;

  Class<?> getClazz();

  List<BeanDescription> getCycle(List<BeanDescription> traversedBeans) throws DIContainerException;
}
