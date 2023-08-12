package ates.homework.auth.config;

import ates.homework.auth.repository.UserRepository;
import ates.homework.auth.verificator.AuthVerificator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    AuthVerificator authVerificator(UserRepository userRepository) {
        return new AuthVerificator(userRepository);
    }
}
