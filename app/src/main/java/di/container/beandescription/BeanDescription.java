package di.container.beandescription;

import di.container.DIContainerException;
import di.container.beaninstance.BeanInstance;
import di.container.beaninstance.PrototypeInstance;
import di.container.beaninstance.SingletonInstance;
import di.container.beaninstance.ThreadInstance;
import di.container.dependency.Dependency;
import di.container.dependency.InjectableConstructor;
import di.container.dependency.InjectableMethod;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanDescription {

  private final BeanLifecycle beanLifecycle;
  private final Class<?> clazz;
  private final List<InjectableConstructor> constructorArgs;
  private final List<InjectableMethod> injectableMethods;
  private final List<Dependency> fieldDependencies;
  private final BeanInstance beanInstance;

  public BeanLifecycle getBeanLifecycle() {
    return beanLifecycle;
  }

  public BeanDescription(BeanLifecycle beanLifecycle, Class<?> clazz,
      List<InjectableConstructor> constructorArgs, List<Dependency> fieldDependencies,
      List<InjectableMethod> injectableMethods) {
    this.beanLifecycle = beanLifecycle;

    beanInstance = switch (beanLifecycle) {
      case THREAD -> new ThreadInstance();
      case PROTOTYPE -> new PrototypeInstance();
      case SINGLETON -> new SingletonInstance();
    };

    this.clazz = clazz;
    this.constructorArgs = constructorArgs;
    this.injectableMethods = injectableMethods;
    this.fieldDependencies = fieldDependencies;
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public Object getBean() throws DIContainerException {
    Object bean = beanInstance.get();
    if (bean == null) {
      bean = generateBean();
      beanInstance.put(bean);
    }
    return bean;
  }

  private Object generateBean() throws DIContainerException {
    Object object;
    if (constructorArgs.size() != 1) {
      throw new DIContainerException(
          "Illegal injectable constructor count (" + constructorArgs.size() + ") for " + clazz);
    }

    try {
      List<Object> args = new ArrayList<>();
      for (Dependency arg : constructorArgs.get(0).getArguments()) {
        args.add(arg.getBean());
      }
      object = getConstructor().newInstance(args.toArray());
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new DIContainerException("Unable to instantiate bean");
    }

    for (InjectableMethod injectableMethod : injectableMethods) {
      String name = injectableMethod.getMethodName();
      Method method;
      List<Class<?>> classes = injectableMethod.getArguments().stream().map(Dependency::getClazz)
          .collect(Collectors.toList());
      try {
        method = getClazz().getDeclaredMethod(name, classes.toArray(new Class<?>[0]));
      } catch (NoSuchMethodException e) {
        throw new DIContainerException("No setter");
      }

      try {
        method.setAccessible(true);
        List<Object> args = new ArrayList<>();
        for (Dependency arg : injectableMethod.getArguments()) {
          args.add(arg.getBean());
        }
        method.invoke(object, args.toArray());
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    for (Dependency entry : fieldDependencies) {
      try {
        Field field = getClazz().getDeclaredField(entry.getFieldName());
        field.setAccessible(true);
        field.set(object, entry.getBean());
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new DIContainerException(
            "Field \"" + entry.getFieldName() + "\" not found in class \"" + getClazz().getName()
                + "\"");
      }
    }

    return object;
  }

  private Constructor<?> getConstructor() throws DIContainerException {
    Constructor<?> constructor = null;

    for (Constructor<?> currentConstructor : clazz.getConstructors()) {
      if (isMatchingConstructor(currentConstructor, constructorArgs.get(0).getArguments())) {
        constructor = currentConstructor;
        break;
      }
    }

    if (constructor == null) {
      throw new DIContainerException(
          "No matching constructor: " + clazz + "; accepting " + constructorArgs.get(0)
              .getArguments().stream().map(Dependency::getClazz).collect(Collectors.toList()));
    }
    return constructor;
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

  public List<BeanDescription> getCycle(List<BeanDescription> traversedBeans)
      throws DIContainerException {
    if (traversedBeans.contains(this)) {
      traversedBeans.add(this);
      return traversedBeans;
    }
    traversedBeans.add(this);
    List<Dependency> dependencies = new ArrayList<>();
    dependencies.addAll(constructorArgs.stream().map(InjectableConstructor::getArguments)
        .collect(Collectors.toList()).stream().flatMap(List::stream).collect(Collectors.toList()));
    dependencies.addAll(
        injectableMethods.stream().map(InjectableMethod::getArguments).collect(Collectors.toList())
            .stream().flatMap(List::stream).collect(Collectors.toList()));
    dependencies.addAll(fieldDependencies);

    for (Dependency dependency : dependencies) {
      List<BeanDescription> cycle = dependency.getCycle(traversedBeans);
      if (cycle != null) {
        return cycle;
      }
    }
    traversedBeans.remove(traversedBeans.size() - 1);
    return null;
  }
}
