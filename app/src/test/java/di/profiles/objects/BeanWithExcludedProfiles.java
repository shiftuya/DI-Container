package di.profiles.objects;

import di.container.annotations.Bean;
import di.container.annotations.Profile;

@Bean
@Profile(exclude = "profile1,profile3")
public class BeanWithExcludedProfiles {

}
