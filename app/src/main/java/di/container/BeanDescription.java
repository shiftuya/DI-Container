package di.container;

import di.container.beanproperty.BeanProperty;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeanDescription {

  private BeanLifecycle beanLifecycle;
  private Class<?> clazz;
  private Object instance;
  private boolean isProxy;
  private List<BeanProperty> constructorArgs;
  private List<BeanProperty> setterArgs;

  public BeanDescription(BeanLifecycle beanLifecycle, Class<?> clazz, boolean isProxy,
                         List<BeanProperty> constructorArgs, List<BeanProperty> setterArgs) {
    this.beanLifecycle = beanLifecycle;
    this.clazz = clazz;
    this.isProxy = isProxy;
    this.constructorArgs = constructorArgs;
    this.setterArgs = setterArgs;
  }

  // todo -o why private getters?
  // todo -o why unused getters and setters?
  private BeanLifecycle getBeanLifecycle() {
    return beanLifecycle;
  }

  public Class<?> getClazz() {
    return clazz;
  }

  private Object getInstance() {
    return instance;
  }

  public boolean isProxy() {
    return isProxy;
  }

  public List<BeanProperty> getConstructorArgs() {
    return constructorArgs;
  }

  private void setInstance(Object instance) {
    this.instance = instance;
  }

  public List<BeanProperty> getSetterArgs() {
    return setterArgs;
  }

  public void setBeanLifecycle(BeanLifecycle beanLifecycle) {
    this.beanLifecycle = beanLifecycle;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  public void setProxy(boolean proxy) {
    isProxy = proxy;
  }

  public Object getBean() throws DIContainerException {
    Object bean;
    switch (getBeanLifecycle()) {
      case SINGLETON -> {
        if (getInstance() == null) {
          setInstance(generateBean());
        }
        bean = getInstance();
      }
      case PROTOTYPE -> bean = generateBean();
      case THREAD -> throw new DIContainerException("I don't know what is that yet");
      default -> throw new DIContainerException("Unknown lifecycle");
    }
    return bean;
  }

  private Object generateBean() throws DIContainerException {
    var constructor = getConstructor(); // todo -o var used only once
    Object object;
    try {
      List<Object> args = new ArrayList<>(); // todo what to do - can't throw exceptions from lambda; todo -o what???
      for (BeanProperty arg : getConstructorArgs()) {
        args.add(arg.getBean());
      }
      object = constructor.newInstance(args.toArray());
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new DIContainerException("Unable to instantiate bean");
    }

    for (BeanProperty arg : getSetterArgs()) {
      String name = "set" + arg.getFieldName().substring(0, 1).toUpperCase() + arg.getFieldName().substring(1); // todo -o move to Utils.capitalize()
      Method method;
      try {
        method = getClazz().getMethod(name, arg.getClazz());
      } catch (NoSuchMethodException e) {
        throw new DIContainerException("No setter");
      }

      try {
        method.invoke(object, arg.getBean());
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    return object;
  }

  private Constructor<?> getConstructor() {
    var constructors = getClazz().getConstructors(); // todo -o var used only once

    Constructor<?> constr = null;

    for (var constructor : constructors) {
      if (constructor.isVarArgs()) {
        // ... TODO
      } else { // todo -o else if (...) {
        if (isMatchingConstructor(constructor, getConstructorArgs())) {
          constr = constructor;
          break;
        }
      }
    }

    return constr;
  }

  private boolean isMatchingConstructor(Constructor<?> constructor, List<BeanProperty> args) {
    if (constructor.getParameterCount() != args.size()) {
      return false;
    }
    for (int i = 0; i < constructor.getParameterCount(); ++i) {
      if (!constructor.getParameterTypes()[i].isAssignableFrom(args.get(i).getClazz())) {
        return false;
      }
    }
    return true;
  }
}
