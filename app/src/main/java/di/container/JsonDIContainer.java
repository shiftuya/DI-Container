package di.container;

import di.beanparser.BeanParserException;
import di.beanparser.json.JsonBeanParser;

public class JsonDIContainer extends GenericDIContainer {
  public JsonDIContainer(String filename) throws DIContainerException {
    try {
      beanFactory = new JsonBeanParser(filename).getBeanFactory();
      beanFactory.initSingletons();
      beanFactory.checkForCircularDependency();
    } catch (BeanParserException e) {
      throw new DIContainerException("Unable to parse file: " + filename + " " + e);
    }
  }
}
