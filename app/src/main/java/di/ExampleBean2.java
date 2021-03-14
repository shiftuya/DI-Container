package di;

public class ExampleBean2 {

  public void setintegerProperty(int integerProperty) {
    System.out.println("small");
    this.integerProperty = integerProperty;
  }


  public void setIntegerProperty(int integerProperty) {
    System.out.println("Big");
    this.integerProperty = integerProperty;
  }



  public int integerProperty;
}
