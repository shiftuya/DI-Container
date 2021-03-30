package di.annotations.objects;

import di.container.annotations.Bean;

@Bean
public class ClassWithoutInjectedConstructor {

  private final EmptyClass dependency;

  public ClassWithoutInjectedConstructor(EmptyClass dependency) {
    this.dependency = dependency;
  }

  public EmptyClass getDependency() {
    return dependency;
  }
}
