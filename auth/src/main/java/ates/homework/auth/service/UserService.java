package ates.homework.auth.service;

import ates.homework.auth.entity.User;
import ates.homework.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
