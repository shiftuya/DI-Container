package di;

import di.beanparser.AnnotationBeanParser;
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
  public void jsonContainerTest() throws DIContainerException, ClassNotFoundException, IOException, URISyntaxException {
    AnnotationBeanParser annotationBeanParser = new AnnotationBeanParser("", AnnotationContainerTest.class);

    DIContainer diContainer = new GenericDIContainer(annotationBeanParser.getBeanFactory());

    Person person = diContainer.getBean(Person.class);
    System.out.println(person.getFullName().getFirstName());
  }
}
