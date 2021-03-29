package di.beanparser.profile;

import di.container.ContainerConstants;
import di.util.Utils;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleProfileChecker implements ProfileChecker {

  @Override
  public boolean matchingProfiles(String includeProfiles, String excludeProfiles) {
    Set<String> profiles = Utils
        .splitByComma(System.getProperty(ContainerConstants.PROFILES_PROPERTY_NAME)).stream()
        .collect(Collectors.toUnmodifiableSet());
    if (!profiles.containsAll(Utils.splitByComma(includeProfiles))) {
      return false;
    }
    return Collections.disjoint(Utils.splitByComma(excludeProfiles), profiles);
  }
}
