package ates.homework.accounting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private int amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne()
    @JoinColumn(name = "user_public_id")
    private User user;

    private String description;

    private String taskPublicId;
}
