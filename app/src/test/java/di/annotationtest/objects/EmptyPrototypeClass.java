package di.annotationtest.objects;

import di.container.beandescription.BeanLifecycle;
import di.container.annotations.Bean;

@Bean(lifecycle = BeanLifecycle.PROTOTYPE)
public class EmptyPrototypeClass {

}
