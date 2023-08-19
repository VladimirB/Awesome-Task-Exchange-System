package ates.homework.accounting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(name = "public_id")
    private String publicId;

    private String login;

    private UserRole role;

    public User(String publicId, String login, UserRole role) {
        this.publicId = publicId;
        this.login = login;
        this.role = role;
    }
}
