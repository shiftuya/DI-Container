package di.container;

import di.beanparser.JsonBeanParser;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class JsonDIContainer implements DIContainer {

  private final BeanFactory beanFactory;

  public JsonDIContainer(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public JsonDIContainer(String filename) throws DIContainerException {
    try {
      beanFactory = new JsonBeanParser(filename).getBeanFactory();
    } catch (IOException | ClassNotFoundException e) {
      throw new DIContainerException("Unable to parse file: " + filename);
    }
  }

  @Override
  public <T> T getBean(String id, Class<T> clazz) throws DIContainerException {
    Object bean = getBean(id);
    if (clazz.isInstance(bean)) {
      return (T)bean;
    }
    throw new DIContainerException(id + " is not " + clazz.getName());
  }

  @Override
  public Object getBean(String id) throws DIContainerException {
    return beanFactory.getBean(id);
  }

  @Override
  public Collection<String> getBeans() {
    return new HashSet<>(beanFactory.getBeanDescriptions().keySet());
  }

  @Override
  public <T> T getBean(Class<T> clazz) throws DIContainerException {
    return (T)beanFactory.getBean(clazz);
  }
}
