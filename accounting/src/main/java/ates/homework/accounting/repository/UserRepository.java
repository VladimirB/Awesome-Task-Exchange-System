package ates.homework.accounting.repository;

import ates.homework.accounting.entity.User;
import ates.homework.accounting.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByPublicId(String publicId);

    List<User> findAllByRole(UserRole role);
}
