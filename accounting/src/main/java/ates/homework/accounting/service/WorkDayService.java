package ates.homework.accounting.service;

import ates.homework.accounting.entity.*;
import ates.homework.accounting.repository.AccountRepository;
import ates.homework.accounting.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WorkDayService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public WorkDayService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void completeDay(User user) throws IllegalStateException {
        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.MANAGER) {
            throw new IllegalStateException("Only Admin and Manager can complete a work day");
        }

        var accounts = accountRepository.findAll();
        accounts.forEach(account -> {
            if (account.getBalance() > 0) {
                paySalary(account);
            } else {
                makeLoss(account);
            }
        });
    }

    @Transactional
    private void paySalary(Account account) {
        var balance = account.getBalance();

        account.setBalance(0);
        accountRepository.save(account);

        var transaction = new Transaction();
        transaction.setAmount(balance);
        transaction.setType(TransactionType.SALARY);
        transaction.setUser(account.getUser());
        transaction.setDate(new Date());
        transaction.setDescription("Выплата зарплаты");
        transactionRepository.save(transaction);
    }

    private void makeLoss(Account account) {
        var transaction = new Transaction();
        transaction.setAmount(account.getBalance());
        transaction.setType(TransactionType.LOSS_FROM_PREV_DAY);
        transaction.setUser(account.getUser());
        transaction.setDate(new Date());
        transaction.setDescription("Перенос убытка на следующий день");
        transactionRepository.save(transaction);
    }
}
