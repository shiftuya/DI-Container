package di.profiles.objects;

import di.container.annotations.Bean;
import di.container.annotations.Profile;

@Bean
@Profile(include = "profile1,profile2")
public class BeanWithIncludedProfiles {

}
