package di.container;

import di.beanparser.JsonBeanParser;
import java.io.IOException;

public class JsonDIContainer extends GenericDIContainer {
  public JsonDIContainer(String filename) throws DIContainerException {
    try {
      beanFactory = new JsonBeanParser(filename).getBeanFactory();
    } catch (IOException | ClassNotFoundException e) {
      throw new DIContainerException("Unable to parse file: " + filename);
    }
  }
}
