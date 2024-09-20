package me.adonlic.app.user;

import me.adonlic.app.user.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
//            User ante = new User(
//                    "Ante Đonlić",
//                    "profile_pics/1_Ante"
//            );
//            User vladimir = new User(
//                    "Vladimir Pleština",
//                    "profile_pics/2_Vladimir"
//                    );
//
//            userRepository.saveAll(
//                    List.of(ante, vladimir)
//            );
        };
    }
}
