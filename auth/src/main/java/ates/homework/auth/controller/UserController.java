package ates.homework.auth.controller;

import ates.homework.auth.dto.UserDto;
import ates.homework.auth.entity.User;
import ates.homework.auth.entity.UserRole;
import ates.homework.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(it -> new UserDto(it.getLogin(), it.getPassword(), it.getRole().name()))
                .toList();
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDto newUser) {
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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }
}
