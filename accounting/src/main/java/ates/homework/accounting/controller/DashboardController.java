package ates.homework.accounting.controller;

import ates.homework.accounting.auth.AuthVerificator;
import ates.homework.accounting.dto.AccountShortDto;
import ates.homework.accounting.dto.TotalRevenueReport;
import ates.homework.accounting.entity.Transaction;
import ates.homework.accounting.entity.TransactionType;
import ates.homework.accounting.entity.User;
import ates.homework.accounting.entity.UserRole;
import ates.homework.accounting.repository.AccountRepository;
import ates.homework.accounting.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final AuthVerificator authVerificator;

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    public DashboardController(AuthVerificator authVerificator, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.authVerificator = authVerificator;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Отчет: Сколько заработал топ-менеджмент за сегодня и сколько попугов ушло в минус
     */
    @GetMapping("/total_revenue")
    public ResponseEntity<Object> getTotalRevenueReport(@RequestHeader("x-auth-token") String token) {
        User user;
        try {
            user = authVerificator.verifyUserByToken(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Only Admin can see the report");
        }

        var localDate = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(localDate, LocalTime.MAX);
        var revenue = transactionRepository.findAllByType(
                List.of(TransactionType.PAYOUT.name(), TransactionType.PENALTY.name()),
                        startOfDay,
                        endOfDay)
                .stream()
                .mapToInt(Transaction::getAmount)
                .sum();

        var lossAccounts = accountRepository.findAllWithLosses()
                .stream()
                .map(it -> new AccountShortDto(it.getUser().getLogin(), it.getBalance()))
                .toList();

        var report = new TotalRevenueReport(revenue * -1, lossAccounts);
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    /**
     * Показывать самую дорогую задачу за день
     */
    @GetMapping("/expensive_task")
    public ResponseEntity<Object> getTodayMostExpensiveTask(@RequestHeader("x-auth-token") String token) {
        User user;
        try {
            user = authVerificator.verifyUserByToken(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Only Admin can see the report");
        }

        var localDate = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(localDate, LocalTime.MAX);
        var transaction = transactionRepository.findAllByType(
                List.of(TransactionType.PAYOUT.name()),
                startOfDay,
                endOfDay)
                .stream()
                .max(Comparator.comparing(Transaction::getAmount));

        return ResponseEntity.status(HttpStatus.OK)
                .body(transaction);
    }
}
