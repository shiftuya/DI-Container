package di.annotations.objects;

import di.container.annotations.Bean;

@Bean
public class AnotherInterfaceImpl implements Interface {
  @Override
  public String getString() {
    return "AnotherInterfaceImpl";
  }
}
