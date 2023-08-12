package ates.homework.auth.controller;

import ates.homework.auth.broker.MessageBroker;
import ates.homework.auth.config.KafkaProducerConfig;
import ates.homework.auth.dto.UserDto;
import ates.homework.auth.entity.User;
import ates.homework.auth.entity.UserRole;
import ates.homework.auth.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    public static final String X_AUTH_TOKEN_HEADER = "x-auth-token";

    private final UserService userService;

    private final MessageBroker messageBroker;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserController(UserService userService, MessageBroker messageBroker, KafkaTemplate<String, String> kafkaTemplate) {
        this.userService = userService;
        this.messageBroker = messageBroker;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@RequestHeader(X_AUTH_TOKEN_HEADER) String token) {
//        try {
//            if (!isUserAllowed(token)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(e.getMessage());
//        }

        messageBroker.sendMessage("Hello World!", KafkaProducerConfig.TOPIC_USERS_STREAM);

        var users = userService.getAllUsers()
                .stream()
                .map(it -> new UserDto(it.getLogin(), it.getPassword(), it.getRole().name()))
                .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(users);
    }

    private boolean isUserAllowed(String token) throws IllegalStateException {
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

        var user = userService.findByLogin(login);
        return user.isPresent() && user.get().getRole() == role;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDto newUser) throws JsonProcessingException {
        if (!StringUtils.hasText(newUser.login()) || !StringUtils.hasText(newUser.password())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Login and password should not be empty");
        }

        UserRole role;
        try {
            role = UserRole.valueOf(newUser.role());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Incorrect role. Allowed: " + Arrays.toString(UserRole.values()));
        }

        var savedUser = userService.findByLogin(newUser.login());
        if (savedUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists");
        }

        var user = new User();
        user.setLogin(newUser.login());
        user.setPassword(newUser.password());
        user.setRole(role);
        user.setPublicId(UUID.randomUUID().toString());

        var createdUser = userService.createUser(user);
        messageBroker.sendMessage(objectMapper.writeValueAsString(createdUser), KafkaProducerConfig.TOPIC_USERS_STREAM);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUser);
    }
}
