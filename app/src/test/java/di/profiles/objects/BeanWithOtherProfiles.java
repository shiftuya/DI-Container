package di.profiles.objects;

import di.container.annotations.Bean;
import di.container.annotations.Profile;

@Bean
@Profile(include = "profile4")
public class BeanWithOtherProfiles {

}
