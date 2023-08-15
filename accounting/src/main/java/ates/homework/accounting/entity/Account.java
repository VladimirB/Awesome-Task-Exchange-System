package ates.homework.accounting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int balance;

    @OneToOne
    @JoinColumn(name = "public_id")
    private User user;

    public Account(User user) {
        this.user = user;
        this.balance = 0;
    }
}
