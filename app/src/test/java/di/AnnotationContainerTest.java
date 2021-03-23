package di;

import di.beanparser.AnnotationBeanParser;
import di.beanparser.BeanParserException;
import di.container.DIContainer;
import di.container.DIContainerException;
import di.container.GenericDIContainer;
import di.jsonparser.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnnotationContainerTest {

  @Test
  public void jsonContainerTest() throws DIContainerException, BeanParserException {
    DIContainer diContainer = new GenericDIContainer(new AnnotationBeanParser(AnnotationContainerTest.class).getBeanFactory());

    Person person = diContainer.getBean(Person.class);

    assertEquals("Ivan", person.getFullName().getFirstName());
    assertEquals("Shatalov", person.getFullName().getSecondName());
  }
}
