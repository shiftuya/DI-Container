package di.annotations.objects;

import di.container.annotations.Bean;

@Bean
public class ClassWithoutInjectedConstructor {
  public ClassWithoutInjectedConstructor(EmptyClass dependency) {
  }
}
