package di.profiles.objects;

import di.container.annotations.Bean;
import di.container.annotations.Profile;

@Bean
@Profile(include = "profile1", exclude = "profile2")
public class BeanWithAllProfiles {

}
