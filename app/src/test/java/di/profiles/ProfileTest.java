package di.profiles;

import static org.junit.Assert.assertNotNull;

import di.container.AnnotationDIContainer;
import di.container.ContainerConstants;
import di.container.DIContainer;
import di.container.DIContainerException;
import di.profiles.objects.BeanWithAllProfiles;
import di.profiles.objects.BeanWithExcludedProfiles;
import di.profiles.objects.BeanWithIncludedProfiles;
import di.profiles.objects.BeanWithOtherProfiles;
import di.profiles.objects.BeanWithOverlappingProfiles;
import di.profiles.objects.BeanWithoutProfiles;
import org.junit.Test;

public class ProfileTest {
  DIContainer container;

  public ProfileTest() throws DIContainerException {
    System.setProperty(ContainerConstants.PROFILES_PROPERTY_NAME, "profile1,profile2,profile3");
    container = new AnnotationDIContainer(this.getClass());
  }

  @Test
  public void beanWithoutProfilesTest() throws DIContainerException {
    assertNotNull(container.getBean(BeanWithoutProfiles.class));
  }

  @Test
  public void beanWithIncludedProfilesTest() throws DIContainerException {
    assertNotNull(container.getBean(BeanWithIncludedProfiles.class));
  }

  @Test(expected = DIContainerException.class)
  public void beanWithExcludedProfilesTest() throws DIContainerException {
    container.getBean(BeanWithExcludedProfiles.class);
  }

  @Test(expected = DIContainerException.class)
  public void beanWithAllProfilesTest() throws DIContainerException {
    container.getBean(BeanWithAllProfiles.class);
  }

  @Test(expected = DIContainerException.class)
  public void beanWithOtherProfilesTest() throws DIContainerException {
    container.getBean(BeanWithOtherProfiles.class);
  }

  @Test(expected = DIContainerException.class)
  public void beanWithOverlappingProfilesTest() throws DIContainerException {
    container.getBean(BeanWithOverlappingProfiles.class);
  }
}
