package di;

import di.beanparser.AnnotationBeanParser;
import di.beanparser.BeanParser;
import di.container.BeanFactory;
import di.container.DIContainer;
import di.container.DIContainerException;
import di.container.GenericDIContainer;
import di.container.JsonDIContainer;
import di.jsonparser.FullName;
import di.jsonparser.IPassport;
import di.jsonparser.Person;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AnnotationContainerTest {

  @Test
  public void jsonContainerTest() throws DIContainerException, IOException, URISyntaxException {
    DIContainer diContainer = new GenericDIContainer(new AnnotationBeanParser(AnnotationContainerTest.class).getBeanFactory());

    Person person = diContainer.getBean(Person.class);

    assertEquals("Ivan", person.getFullName().getFirstName());
    assertEquals("Shatalov", person.getFullName().getSecondName());
  }
}
