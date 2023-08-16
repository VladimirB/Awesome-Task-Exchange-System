package ates.homework.task_tracker.entity;

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
    private long id;

    @Column(name = "public_id")
    private String publicId;

    private String title;

    private TaskStatus status;

    @Column(name = "payout_amount")
    private int payoutAmount;

    @Column(name = "penalty_amount")
    private int penaltyAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task(String publicId, String title, TaskStatus status, int payoutAmount, int penaltyAmount, User user) {
        this.publicId = publicId;
        this.title = title;
        this.status = status;
        this.payoutAmount = payoutAmount;
        this.penaltyAmount = penaltyAmount;
        this.user = user;
    }
}
