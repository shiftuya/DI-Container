package di.container;

import di.beanparser.AnnotationBeanParser;
import java.io.IOException;
import java.net.URISyntaxException;

public class AnnotationDIContainer extends GenericDIContainer {
  public AnnotationDIContainer(Class<?>... startupClasses) throws DIContainerException {
    try {
      beanFactory = new AnnotationBeanParser(startupClasses).getBeanFactory();
    } catch (DIContainerException | URISyntaxException | IOException e) {
      throw new DIContainerException("Can't create");
    }
  }
}
