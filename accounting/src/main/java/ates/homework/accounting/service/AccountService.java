package ates.homework.accounting.service;

import ates.homework.accounting.entity.Account;
import ates.homework.accounting.entity.Transaction;
import ates.homework.accounting.entity.TransactionType;
import ates.homework.accounting.entity.User;
import ates.homework.accounting.event.TaskWasAssignedEvent;
import ates.homework.accounting.event.TaskWasCompletedEvent;
import ates.homework.accounting.repository.AccountRepository;
import ates.homework.accounting.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void createAccount(User user) {
        var account = new Account(user);
        accountRepository.save(account);
    }

    public Optional<Account> getAccountByUserPublicId(String publicId) {
        return accountRepository.findByUserPublicId(publicId);
    }

    @Transactional
    public void upBalance(TaskWasCompletedEvent event, Date eventDate) {
        var account = getAccountByUserPublicId(event.userPublicId());
        account.ifPresentOrElse(it -> {
            it.setBalance(it.getBalance() + event.payout());
            accountRepository.save(it);

            var transaction = new Transaction();
            transaction.setAmount(event.payout());
            transaction.setType(TransactionType.PAYOUT);
            transaction.setUser(it.getUser());
            transaction.setDate(eventDate);
            transactionRepository.save(transaction);
        }, () -> {
            throw new IllegalStateException("Not found account for user: " + event.userPublicId());
        });
    }

    @Transactional
    public void downBalance(TaskWasAssignedEvent event, Date eventDate) {
        var account = getAccountByUserPublicId(event.userPublicId());
        account.ifPresentOrElse(it -> {
            it.setBalance(it.getBalance() + event.penalty());
            accountRepository.save(it);

            var transaction = new Transaction();
            transaction.setAmount(event.penalty());
            transaction.setType(TransactionType.PENALTY);
            transaction.setUser(it.getUser());
            transaction.setDate(eventDate);
            transactionRepository.save(transaction);
        }, () -> {
            throw new IllegalStateException("Not found account for user: " + event.userPublicId());
        });
    }
}
