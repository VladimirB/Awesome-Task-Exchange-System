package ates.homework.accounting.repository;

import ates.homework.accounting.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByUserId(long userId);

    @Query(value = "SELECT * FROM transactions WHERE type IN (?1) AND date BETWEEN ?2 AND ?3",
            nativeQuery = true)
    Collection<Transaction> findAllByType(List<String> types, LocalDateTime start, LocalDateTime end);
}
