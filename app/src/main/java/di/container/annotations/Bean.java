package di.container.annotations;

import di.container.BeanLifecycle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    String id() default "";
    BeanLifecycle lifecycle() default BeanLifecycle.SINGLETON;
}
