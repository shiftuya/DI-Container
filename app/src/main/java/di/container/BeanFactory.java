package di.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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

    Object bean;

    switch(beanDescription.getBeanLifecycle()) {
      case SINGLETON:
        if (beanDescription.getInstance() == null) {
          beanDescription.setInstance(generateBean(beanDescription));
        }
        bean = beanDescription.getInstance();
        break;
      case PROTOTYPE:
        bean = generateBean(beanDescription);
        break;
      case THREAD:
        throw new DIContainerException("I don't know what is that yet");
      default:
        throw new DIContainerException("Unknown lifecycle");
    }
    return bean;
  }

  public Object generateBean(BeanDescription description) throws DIContainerException {
    var constr = getConstructor(description);
    Object object = null;
    try {
      List<Object> args = new ArrayList<>(); // todo what to do about this
      for (var arg : description.getConstructorArgs()) {
        args.add(arg.getBean());
      }
      object = constr.newInstance(args.toArray());
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new DIContainerException("Unable to instantiate bean");
    }

    for (var arg : description.getSetterArgs()) {
      String name = "set" + arg.getName().substring(0, 1).toUpperCase() + arg.getName().substring(1);
      Method method;
      try {
        method = description.getClazz().getMethod(name, arg.getClazz());
      } catch (NoSuchMethodException e) {
        throw new DIContainerException("No setter");
      }

      try {
        method.invoke(object, arg.getBean());
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

   // description.getClazz().getMethod("set"+)

    return object;
  }

  private Constructor<?> getConstructor(BeanDescription description) {
    var constructors = description.getClazz().getConstructors();

    Constructor<?> constr = null;

    for (var constructor : constructors) {
      if (constructor.isVarArgs()) {
        // ... TODO
      } else {
        if (isMatchingConstructor(constructor, description.getConstructorArgs())) {
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
