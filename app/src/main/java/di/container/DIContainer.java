package di.container;

import java.util.Collection;

public interface DIContainer {
  <T> T getBean(String name, Class<T> clazz);

  Object getBean(String name);

  Collection<String> getBeans();
}
