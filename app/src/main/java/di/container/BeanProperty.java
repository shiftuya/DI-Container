package di.container;

public interface BeanProperty {
  String getId();
  Object getBean() throws DIContainerException;
  Class<?> getClazz();
}
