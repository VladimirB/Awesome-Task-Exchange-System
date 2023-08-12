package ates.homework.task_tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private String description;

    private TaskStatus status;

    @Column(name = "payout_amount")
    private int payoutAmount;

    @Column(name = "penalty_amount")
    private int penaltyAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "public_id")
    private User user;

    public Task(String description, TaskStatus status, int payoutAmount, int penaltyAmount, User user) {
        this.description = description;
        this.status = status;
        this.payoutAmount = payoutAmount;
        this.penaltyAmount = penaltyAmount;
        this.user = user;
    }
}
