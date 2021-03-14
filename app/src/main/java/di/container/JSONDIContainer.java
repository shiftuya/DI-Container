package di.container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSONDIContainer implements DIContainer {

  BeanFactory beanFactory = new BeanFactory();
 // private final Map<String, BeanDescription> beans = new ConcurrentHashMap<>();

  @Override
  public <T> T getBean(String name, Class<T> clazz) throws DIContainerException {
    Object bean = getBean(name);
    if (clazz.isInstance(bean)) {
      return (T)bean;
    }
    throw new DIContainerException(name + " is not " + clazz.getName());
  }

  public JSONDIContainer(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public JSONDIContainer() {

  }

  @Override
  public Object getBean(String name) throws DIContainerException {
   /* BeanDescription beanDescription = beans.get(name);
    if (beanDescription == null) {
      throw new DIContainerException("Illegal name!!!");
    }

    switch(beanDescription.getBeanLifecycle()) {
      case SINGLETON:
        if (beanDescription.getInstance() == null) {
          //...
        } else {
        }
        break;
      case PROTOTYPE:

        break;
      case THREAD:
        break;
      default:
        throw new DIContainerException("Unknown lifecycle");
    }

*/

    return beanFactory.getBean(name);
  }

  @Override
  public Collection<String> getBeans() {
    return null;
  }
}
