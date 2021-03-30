package di.annotations.objects;

import di.container.beandescription.BeanLifecycle;
import di.container.annotations.Bean;
import javax.inject.Named;

@Bean(lifecycle = BeanLifecycle.PROTOTYPE)
@Named(value = "InterfaceImpl")
public class InterfaceImpl implements Interface {

  @Override
  public String getString() {
    return "InterfaceImpl";
  }
}
