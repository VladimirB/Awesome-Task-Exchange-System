package ates.homework.accounting.service;

import ates.homework.accounting.entity.Account;
import ates.homework.accounting.entity.User;
import ates.homework.accounting.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(User user) {
        var account = new Account(user);
        accountRepository.save(account);
    }

    public Optional<Account> getAccountByUser(User user) {
        return accountRepository.findByUserPublicId(user.getPublicId());
    }
}
