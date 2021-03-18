package di.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

  private Map<String, BeanDescription> beans; // todo -o BeanDescriptions

  public Object getBean(String id) throws DIContainerException {
    var beanDescription = beans.get(id);
    if (beanDescription == null) {
      throw new DIContainerException("Illegal id!!!");
    }

    return beanDescription.getBean();
  }

  public BeanDescription getBeanDescription(String id) {
    return beans.get(id);
  }

  public Map<String, BeanDescription> getBeans() {
    return beans;
  }

  public void setBeans(Map<String, BeanDescription> beans) {
    this.beans = new ConcurrentHashMap<>(beans);
  }
}
