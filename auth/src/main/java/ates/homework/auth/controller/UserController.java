package ates.homework.auth.controller;

import ates.homework.auth.broker.EventSender;
import ates.homework.auth.config.KafkaProducerConfig;
import ates.homework.auth.dto.UserDto;
import ates.homework.auth.entity.User;
import ates.homework.auth.entity.UserRole;
import ates.homework.auth.event.EventWrapper;
import ates.homework.auth.event.UserWasCreatedEvent;
import ates.homework.auth.service.UserService;
import ates.homework.auth.verificator.AuthVerificator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    public static final String X_AUTH_TOKEN_HEADER = "x-auth-token";

    private final UserService userService;

    private final EventSender eventSender;

    private final AuthVerificator authVerificator;

    public UserController(UserService userService, EventSender eventSender, AuthVerificator authVerificator) {
        this.userService = userService;
        this.eventSender = eventSender;
        this.authVerificator = authVerificator;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@RequestHeader(X_AUTH_TOKEN_HEADER) String token) {
        try {
            authVerificator.verifyUserByToken(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        var users = userService.getAllUsers()
                .stream()
                .map(it -> new UserDto(it.getLogin(), it.getPassword(), it.getRole().name()))
                .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(users);
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
        var event = new UserWasCreatedEvent(createdUser.getPublicId(), createdUser.getLogin(), createdUser.getRole());
        eventSender.sendEvent(new EventWrapper<>(event), KafkaProducerConfig.TOPIC_USERS_STREAM);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUser);
    }
}
