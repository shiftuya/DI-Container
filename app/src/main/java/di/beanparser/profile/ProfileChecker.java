package di.beanparser.profile;

public interface ProfileChecker {
  boolean matchingProfiles(String includeProfiles, String excludeProfiles);
}
