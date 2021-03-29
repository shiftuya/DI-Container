package di.container;

import di.beanparser.BeanParserException;
import di.beanparser.annotation.AnnotationBeanParser;
import di.beanparser.json.JsonBeanParser;

public class AnnotationJsonDIContainer extends GenericDIContainer {

  public AnnotationJsonDIContainer(String json, Class<?>... startupClasses)
      throws DIContainerException {
    try {
      beanFactory = new AnnotationBeanParser(startupClasses).getBeanFactory()
          .merge(new JsonBeanParser(json).getBeanFactory());
      beanFactory.initSingletons();
      beanFactory.checkForCircularDependency();
    } catch (BeanParserException e) {
      throw new DIContainerException(e.getMessage());
    }
  }
}
