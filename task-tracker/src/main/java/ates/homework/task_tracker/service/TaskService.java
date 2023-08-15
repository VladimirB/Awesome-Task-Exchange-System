package ates.homework.task_tracker.service;

import ates.homework.task_tracker.entity.Task;
import ates.homework.task_tracker.entity.TaskStatus;
import ates.homework.task_tracker.entity.User;
import ates.homework.task_tracker.entity.UserRole;
import ates.homework.task_tracker.repository.TaskRepository;
import ates.homework.task_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findAllByUserPublicId(user.getPublicId());
    }

    public Task createTask(Task task) {
        Random random = new Random();
        var payout = random.nextInt(20) + 20;
        var penalty = random.nextInt(10) - 20;

        var users = userRepository.findAllByRole(UserRole.POPUG);
        var index = random.nextInt(users.size());

        var newTask = new Task(task.getTitle(), TaskStatus.OPEN, payout, penalty, users.get(index));
        return taskRepository.save(newTask);
    }
}
