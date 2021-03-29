package di.container;

import di.beanparser.annotation.AnnotationBeanParser;
import di.beanparser.BeanParserException;

public class AnnotationDIContainer extends GenericDIContainer {
  public AnnotationDIContainer(Class<?>... startupClasses) throws DIContainerException {
    try {
      beanFactory = new AnnotationBeanParser(startupClasses).getBeanFactory();
      beanFactory.initSingletons();
    } catch (BeanParserException e) {
      throw new DIContainerException(e.getMessage());
    }
  }
}
