package ates.homework.auth.verificator;

import ates.homework.auth.entity.User;
import ates.homework.auth.entity.UserRole;
import ates.homework.auth.repository.UserRepository;

import java.util.Arrays;

public class AuthVerificator {

    private final UserRepository userRepository;

    public AuthVerificator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User verifyUserByToken(String token) throws IllegalStateException {
        var creds = token.split(":");
        if (creds.length != 2) {
            throw new IllegalStateException("Credentials is incorrect. Format: 'login:role'");
        }

        var login = creds[0];
        UserRole role;
        try {
            role = UserRole.valueOf(creds[1]);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Incorrect role. Allowed: " + Arrays.toString(UserRole.values()));
        }

        var user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalStateException("User not found");
        }
    }
}
