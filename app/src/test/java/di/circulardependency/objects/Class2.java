package di.circulardependency.objects;

public class Class2 {
  private final Class1 class1;

  public Class2(Class1 class1) {
    this.class1 = class1;
  }

  public Class1 getClass1() {
    return class1;
  }
}
