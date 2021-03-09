package di.container;

import java.util.ArrayList;
import java.util.List;

public class BeanDescription {
  private BeanLifecycle beanLifecycle;

  private Class<?> clazz;

  private Object instance;

  private boolean isProxy;

  private final List<BeanProperty> constructorArgs = new ArrayList<>();

  private final List<BeanProperty> setterArgs = new ArrayList<>();

  public BeanLifecycle getBeanLifecycle() {
    return beanLifecycle;
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public Object getInstance() {
    return instance;
  }

  public boolean isProxy() {
    return isProxy;
  }

  public List<BeanProperty> getConstructorArgs() {
    return constructorArgs;
  }

  public List<BeanProperty> getSetterArgs() {
    return setterArgs;
  }
}
