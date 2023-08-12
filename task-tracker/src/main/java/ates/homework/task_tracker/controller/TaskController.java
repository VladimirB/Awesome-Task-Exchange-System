package ates.homework.task_tracker.controller;

import ates.homework.task_tracker.auth.AuthVerificator;
import ates.homework.task_tracker.entity.Task;
import ates.homework.task_tracker.entity.User;
import ates.homework.task_tracker.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    private final AuthVerificator authVerificator;

    public TaskController(TaskRepository taskRepository, AuthVerificator authVerificator) {
        this.taskRepository = taskRepository;
        this.authVerificator = authVerificator;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTasks(@RequestHeader("x-auth-token") String token) {
        User user;
        try {
            user = authVerificator.obtainUser(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskRepository.findAllByUserPublicId(user.getPublicId()));
    }

    @PostMapping
    public ResponseEntity<Object> createTask(@RequestBody Task body, @RequestHeader("x-auth-token") String token) {
        User user;
        try {
            user = authVerificator.obtainUser(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        var task = new Task(body.getDescription(), 1, 1, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskRepository.save(task));
    }
}
