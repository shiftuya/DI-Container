package di.annotationtest.objects;

import di.container.beandescription.BeanLifecycle;
import di.container.annotations.Bean;

@Bean(lifecycle = BeanLifecycle.THREAD)
public class EmptyThreadClass {

}
