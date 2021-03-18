package di.container;

import di.container.dependency.Dependency;
import di.util.Utils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeanDescription {

  private final BeanLifecycle beanLifecycle;
  private final Class<?> clazz;
  private Object instance;
  private final boolean isProxy;
  private final List<Dependency> constructorArgs;
  private final List<Dependency> setterArgs;

  public BeanDescription(BeanLifecycle beanLifecycle, Class<?> clazz, boolean isProxy,
      List<Dependency> constructorArgs, List<Dependency> setterArgs) {
    this.beanLifecycle = beanLifecycle;
    this.clazz = clazz;
    this.isProxy = isProxy;
    this.constructorArgs = constructorArgs;
    this.setterArgs = setterArgs;
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public Object getBean() throws DIContainerException {
    Object bean;
    switch (beanLifecycle) {
      case SINGLETON -> {
        if (instance == null) {
          instance = generateBean();
        }
        bean = instance;
      }
      case PROTOTYPE -> bean = generateBean();
      case THREAD -> throw new DIContainerException("I don't know what is that yet");
      default -> throw new DIContainerException("Unknown lifecycle");
    }
    return bean;
  }

  private Object generateBean() throws DIContainerException {
    Object object;
    try {
      List<Object> args = new ArrayList<>();
      for (Dependency arg : constructorArgs) {
        args.add(arg.getBean());
      }
      object = getConstructor().newInstance(args.toArray());
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new DIContainerException("Unable to instantiate bean");
    }

    for (Dependency arg : setterArgs) {
      String name = "set" + Utils.capitalize(arg.getFieldName());
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

  private Constructor<?> getConstructor() throws DIContainerException {
    Constructor<?> constr = null;

    for (Constructor<?> constructor : clazz.getConstructors()) {
      if (constructor.isVarArgs()) {
        // ... TODO
      } else if (isMatchingConstructor(constructor, constructorArgs)) {
        constr = constructor;
        break;
      }
    }

    if (constr == null) {
      throw new DIContainerException("No matching constructor");
    }
    return constr;
  }

  private boolean isMatchingConstructor(Constructor<?> constructor, List<Dependency> args) {
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
