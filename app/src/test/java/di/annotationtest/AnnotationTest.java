package di.annotationtest;

import di.annotationtest.objects.ClassWIthMultipleInjectedConstructors;
import di.annotationtest.objects.ClassWithInjectedConstructor;
import di.annotationtest.objects.ClassWithInjectedMethod;
import di.annotationtest.objects.ClassWithoutInjectedConstructor;
import di.annotationtest.objects.EmptyClass;
import di.annotationtest.objects.EmptyPrototypeClass;
import di.container.AnnotationDIContainer;
import di.container.DIContainer;
import di.container.DIContainerException;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationTest {
  DIContainer container = new AnnotationDIContainer(this.getClass());

  public AnnotationTest() throws DIContainerException {
  }

  @Test
  public void annotatedSingletonTest() throws DIContainerException {
    assertSame(container.getBean(EmptyClass.class), container.getBean(EmptyClass.class));
  }

  @Test
  public void annotatedPrototypeTest() throws DIContainerException {
    assertNotSame(container.getBean(EmptyPrototypeClass.class), container.getBean(EmptyPrototypeClass.class));
  }

  @Test
  public void classWithInjectedConstructorTest() throws DIContainerException {
    assertNotNull(container.getBean(ClassWithInjectedConstructor.class).getDependency());
  }

  @Test
  public void classWithMultipleInjectedConstructorsTest() throws DIContainerException {
    assertThrows(DIContainerException.class, () -> container.getBean(
        ClassWIthMultipleInjectedConstructors.class));
  }

  @Test
  public void classWithoutInjectedConstructorTest() throws DIContainerException {
    assertThrows(DIContainerException.class, () -> container.getBean(
        ClassWithoutInjectedConstructor.class));
  }

  @Test
  public void methodInjectionTest() throws DIContainerException {
    ClassWithInjectedMethod bean = container.getBean(ClassWithInjectedMethod.class);

    assertNotNull(bean.getDependency());
    assertSame(bean.getDependency(), bean.getDependency());
  }

}
