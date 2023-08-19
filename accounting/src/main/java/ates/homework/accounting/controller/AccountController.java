package ates.homework.accounting.controller;

import ates.homework.accounting.auth.AuthVerificator;
import ates.homework.accounting.entity.User;
import ates.homework.accounting.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    private final AuthVerificator authVerificator;

    public AccountController(AccountService accountService, AuthVerificator authVerificator) {
        this.accountService = accountService;
        this.authVerificator = authVerificator;
    }

    @GetMapping
    public ResponseEntity<Object> getAccount(@RequestHeader("x-auth-token") String token) {
        User user;
        try {
            user = authVerificator.verifyUserByToken(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        var account = accountService.getAccountByUserPublicId(user.getPublicId());
        return account.<ResponseEntity<Object>>map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Not found account for user: " + user.getLogin()));
    }
}
