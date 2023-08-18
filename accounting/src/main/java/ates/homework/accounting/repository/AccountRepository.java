package ates.homework.accounting.repository;

import ates.homework.accounting.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserPublicId(String publicId);

    @Query(value = "SELECT * FROM accounts WHERE balance < 0",
            nativeQuery = true)
    List<Account> findAllWithLosses();
}
