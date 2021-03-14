package di;

public class SimpleSingleton {
  private String attribute;

  public SimpleSingleton(String attribute) {
    this.attribute = attribute;
  }

  public String getAttribute() {
    return attribute;
  }
}
