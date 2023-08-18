package ates.homework.task_tracker.service;

import ates.homework.task_tracker.broker.EventSender;
import ates.homework.task_tracker.config.KafkaProducerConfig;
import ates.homework.task_tracker.entity.Task;
import ates.homework.task_tracker.entity.TaskStatus;
import ates.homework.task_tracker.entity.User;
import ates.homework.task_tracker.entity.UserRole;
import ates.homework.task_tracker.event.EventWrapper;
import ates.homework.task_tracker.event.TaskWasAssignedEvent;
import ates.homework.task_tracker.event.TaskWasCompletedEvent;
import ates.homework.task_tracker.repository.TaskRepository;
import ates.homework.task_tracker.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final EventSender eventSender;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, EventSender eventSender) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.eventSender = eventSender;
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findAllByUserPublicId(user.getPublicId());
    }

    public Task createTask(Task task) throws JsonProcessingException {
        Random random = new Random();
        var payout = random.nextInt(20) + 20;
        var penalty = random.nextInt(10) - 20;

        var users = userRepository.findAllByRole(UserRole.POPUG);
        var index = random.nextInt(users.size());

        var newTask = new Task(UUID.randomUUID().toString(),
                task.getTitle(),
                TaskStatus.OPEN,
                payout,
                penalty,
                users.get(index));

        newTask = taskRepository.save(newTask);

        sendTaskAssignedEvent(newTask);

        return newTask;
    }

    private void sendTaskAssignedEvent(Task task) throws JsonProcessingException {
        var event = new TaskWasAssignedEvent(task.getPublicId(),
                task.getTitle(),
                task.getPenaltyAmount(),
                task.getUser().getPublicId());
        eventSender.sendEvent(new EventWrapper<>(event), KafkaProducerConfig.TOPIC_TASK_LIFECYCLE);
    }

    public boolean completeTask(long taskId) throws JsonProcessingException {
        var task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            var updated = task.get();
            updated.setStatus(TaskStatus.DONE);
            taskRepository.save(updated);

            sendTaskDoneEvent(updated);

            return true;
        }

        return false;
    }

    private void sendTaskDoneEvent(Task task) throws JsonProcessingException {
        var event = new TaskWasCompletedEvent(task.getPublicId(),
                task.getTitle(),
                task.getPayoutAmount(),
                task.getUser().getPublicId());
        eventSender.sendEvent(new EventWrapper<>(event), KafkaProducerConfig.TOPIC_TASK_LIFECYCLE);
    }

    public void reassignTasks(User user) throws IllegalStateException, JsonProcessingException {
        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.MANAGER) {
            throw new IllegalStateException("Only Admin or Manager can reassign tasks");
        }

        var users = userRepository.findAllByRole(UserRole.POPUG);
        var tasks = taskRepository.findAllByStatus(TaskStatus.OPEN);

        Random random = new Random();
        List<Task> reassignedTasks = new ArrayList<>();
        tasks.forEach(task -> {
            var index = random.nextInt(users.size());
            task.setUser(users.get(index));
            reassignedTasks.add(task);
        });

        taskRepository.saveAll(reassignedTasks);
        for (Task reassignedTask : reassignedTasks) {
            sendTaskAssignedEvent(reassignedTask);
        }
    }
}
