package di.circulardependency.objects;

public class Class1 {
  private final Class2 class2;

  public Class1(Class2 class2) {
    this.class2 = class2;
  }

  public Class2 getClass1() {
    return class2;
  }
}
