package di;

public class ThingOne {

  public int integerProperty;
  public ThingTwo thingTwo;

  public ThingOne(ThingTwo thingTwo, int integerProperty) {
    this.thingTwo = thingTwo;
    this.integerProperty = integerProperty;
  }

  public void setIntegerProperty(int integerProperty) {
    this.integerProperty = integerProperty;
  }
}
