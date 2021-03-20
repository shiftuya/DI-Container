package di.container.beaninstance;

import java.util.Map;

public class ThreadInstance implements BeanInstance {
  private Map<Thread, Object> threadObjectMap;

  @Override
  public Object get() {
    return threadObjectMap.get(Thread.currentThread());
  }

  @Override
  public void put(Object object) {
    threadObjectMap.put(Thread.currentThread(), object);
  }
}
