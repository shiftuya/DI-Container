package di.container.dependency;

import di.container.DIContainerException;
import javax.inject.Provider;

public class ProviderImpl<T> implements Provider<T> {

  private final Dependency dependency;

  public ProviderImpl(Dependency dependency) {
    this.dependency = dependency;
  }

  @Override
  public T get() {
    try {
      return (T) dependency.getBean();
    } catch (DIContainerException e) {
      e.printStackTrace();
    }
    return null;
  }
}
