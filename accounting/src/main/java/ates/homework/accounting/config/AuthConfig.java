package ates.homework.accounting.config;

import ates.homework.accounting.auth.AuthVerificator;
import ates.homework.accounting.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    AuthVerificator authVerificator(UserRepository userRepository) {
        return new AuthVerificator(userRepository);
    }
}
