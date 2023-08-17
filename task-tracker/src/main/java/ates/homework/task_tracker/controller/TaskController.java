package ates.homework.task_tracker.controller;

import ates.homework.task_tracker.auth.AuthVerificator;
import ates.homework.task_tracker.entity.Task;
import ates.homework.task_tracker.entity.User;
import ates.homework.task_tracker.repository.TaskRepository;
import ates.homework.task_tracker.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final AuthVerificator authVerificator;

    private final TaskService taskService;

    public TaskController(TaskRepository taskRepository, AuthVerificator authVerificator, TaskService taskService) {
        this.authVerificator = authVerificator;
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTasks(@RequestHeader("x-auth-token") String token) {
        User user;
        try {
            user = authVerificator.verifyUserByToken(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTasksByUser(user));
    }

    @PostMapping
    public ResponseEntity<Object> createTask(@RequestBody Task body, @RequestHeader("x-auth-token") String token) throws JsonProcessingException {
        try {
            authVerificator.verifyUserByToken(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(body));
    }

    @PutMapping("/{taskId}/complete")
    public ResponseEntity<Object> completeTask(@RequestHeader("x-auth-token") String token,
                                               @PathVariable long taskId) throws JsonProcessingException {
        try {
            authVerificator.verifyUserByToken(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        var isCompleted = taskService.completeTask(taskId);

        return isCompleted ? ResponseEntity.status(HttpStatus.OK).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found task with id " + taskId);
    }
}
