package di.container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSONDIContainer implements DIContainer {

  private final Map<String, BeanDescription> beans = new ConcurrentHashMap<>();

  @Override
  public <T> T getBean(String name, Class<T> clazz) {
    return null;
  }

  @Override
  public Object getBean(String name) {
    return null;
  }

  @Override
  public Collection<String> getBeans() {
    return null;
  }
}
