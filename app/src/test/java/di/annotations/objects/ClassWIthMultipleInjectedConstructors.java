package di.annotations.objects;

import javax.inject.Inject;

public class ClassWIthMultipleInjectedConstructors {
  @Inject
  public ClassWIthMultipleInjectedConstructors() {

  }

  @Inject
  public ClassWIthMultipleInjectedConstructors(EmptyClass emptyClass) {

  }
}
