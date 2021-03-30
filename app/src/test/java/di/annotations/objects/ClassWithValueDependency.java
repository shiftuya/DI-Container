package di.annotations.objects;

import di.container.annotations.Bean;
import di.container.annotations.Value;
import javax.inject.Inject;

@Bean
public class ClassWithValueDependency {
  @Inject
  @Value("5")
  private int dependency;

  public int getDependency() {
    return dependency;
  }
}
