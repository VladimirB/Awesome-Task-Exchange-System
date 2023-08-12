package ates.homework.task_tracker.config;

import ates.homework.task_tracker.auth.AuthVerificator;
import ates.homework.task_tracker.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    AuthVerificator authVerificator(UserRepository userRepository) {
        return new AuthVerificator(userRepository);
    }
}
