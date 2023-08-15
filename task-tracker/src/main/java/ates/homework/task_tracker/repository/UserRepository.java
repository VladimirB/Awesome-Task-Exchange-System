package ates.homework.task_tracker.repository;

import ates.homework.task_tracker.entity.User;
import ates.homework.task_tracker.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    List<User> findAllByRole(UserRole role);
}
