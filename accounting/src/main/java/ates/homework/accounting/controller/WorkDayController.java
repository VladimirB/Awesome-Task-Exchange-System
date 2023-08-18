package ates.homework.accounting.controller;

import ates.homework.accounting.auth.AuthVerificator;
import ates.homework.accounting.entity.User;
import ates.homework.accounting.service.WorkDayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/close-day")
public class WorkDayController {

    private final WorkDayService workDayService;

    private final AuthVerificator authVerificator;

    public WorkDayController(WorkDayService workDayService, AuthVerificator authVerificator) {
        this.workDayService = workDayService;
        this.authVerificator = authVerificator;
    }

    @PostMapping
    public ResponseEntity<Object> completeDay(@RequestHeader("x-auth-token") String token) {
        User user;
        try {
            user = authVerificator.verifyUserByToken(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        try {
            workDayService.completeDay(user);
            return ResponseEntity.status(HttpStatus.OK).body("Day was completed. Salaries and losses were sent.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}
