package di.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

  public BeanFactory() {
  }

  public BeanFactory(Map<String, BeanDescription> beans) {
    this.beans = beans;
  }

  private Map<String, BeanDescription> beans = new ConcurrentHashMap<>();



  public Object getBean(String name) throws DIContainerException {
    var beanDescription = beans.get(name);
    if (beanDescription == null) {
      throw new DIContainerException("Illegal name!!!");
    }

    return beanDescription.getBean();
  }

}
