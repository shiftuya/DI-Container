package di.container;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BeanFactory {

  private Map<String, BeanDescription> beanDescriptions;

  private Set<BeanDescription> beanDescriptionSet;

  public Object getBean(String id) throws DIContainerException {
    var beanDescription = beanDescriptions.get(id);
    if (beanDescription == null) {
      throw new DIContainerException("Unknown bean id: " + id);
    }

    return beanDescription.getBean();
  }

  public Object getBean(Class<?> clazz) throws DIContainerException {
    List<BeanDescription> descriptions = Sets.union(
        beanDescriptionSet, new HashSet<>(beanDescriptions.values())).stream()
        .filter(description -> clazz.isAssignableFrom(description.getClazz()))
        .collect(Collectors.toList());
    if (descriptions.isEmpty()) {
      throw new DIContainerException("No beans found: " + clazz);
    }
    if (descriptions.size() > 1) {
      throw new DIContainerException("Too many beans found for class " + clazz);
    }
    return descriptions.get(0).getBean();
  }

  public BeanDescription getBeanDescription(String id) {
    return beanDescriptions.get(id);
  }

  public Map<String, BeanDescription> getBeanDescriptions() {
    return beanDescriptions;
  }

  public void setBeanDescriptions(Map<String, BeanDescription> beanDescriptions) {
    this.beanDescriptions = new ConcurrentHashMap<>(beanDescriptions);
  }

  public void setBeanDescriptionSet(Set<BeanDescription> beanDescriptionSet) {
    this.beanDescriptionSet = Collections.synchronizedSet(new HashSet<>(beanDescriptionSet));
  }

  public void initSingletons() throws DIContainerException {
    for (BeanDescription description : Sets.union(
        beanDescriptionSet, new HashSet<>(beanDescriptions.values()))) {
      if (description.getBeanLifecycle() == BeanLifecycle.SINGLETON) {
        try {
          description.getBean();
        } catch (DIContainerException e) {
          throw new DIContainerException(
              "Exception during singleton initialization (" + description.getClazz() + "): " + e
                  .getMessage());
        }
      }
    }
  }

  public BeanFactory merge(BeanFactory other) {
    beanDescriptions.putAll(other.beanDescriptions);
    beanDescriptionSet.addAll(other.beanDescriptionSet);
    return this;
  }
}
