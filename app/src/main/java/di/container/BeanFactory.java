package di.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

  private Map<String, BeanDescription> beanDescriptions;

  public Object getBean(String id) throws DIContainerException {
    var beanDescription = beanDescriptions.get(id);
    if (beanDescription == null) {
      throw new DIContainerException("Illegal id!!!");
    }

    return beanDescription.getBean();
  }

  public BeanDescription getBeanDescription(String id) {
    return beanDescriptions.get(id);
  }

  public Map<String, BeanDescription> getBeanDescriptions() {
    return beanDescriptions;
  }

  public void setBeanDescriptions(Map<String, BeanDescription> beanDescriptions) {
    this.beanDescriptions = new ConcurrentHashMap<>(beanDescriptions);
  }
}
