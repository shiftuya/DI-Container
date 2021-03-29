package di.container.profile;

public interface ProfileChecker {
  boolean matchingProfiles(String includeProfiles, String excludeProfiles);
}
