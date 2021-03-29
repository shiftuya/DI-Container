package di.circulardependency;

import di.container.DIContainer;
import di.container.DIContainerException;
import di.container.JsonDIContainer;
import org.junit.Test;

public class CircularDependencyTest {
  @Test(expected = DIContainerException.class)
  public void circularDependencyTest() throws DIContainerException {
    DIContainer container = new JsonDIContainer("cycles.json");
  }
}
