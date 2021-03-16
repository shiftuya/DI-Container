package di.container;

public interface BeanProperty {
  String getName();
  Object getBean() throws DIContainerException;
  Class<?> getClazz();
}
