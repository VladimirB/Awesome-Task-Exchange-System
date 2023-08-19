package ates.homework.task_tracker.repository;

import ates.homework.task_tracker.entity.Task;
import ates.homework.task_tracker.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUserPublicId(String userPublicId);

    List<Task> findAllByStatus(TaskStatus status);
}
