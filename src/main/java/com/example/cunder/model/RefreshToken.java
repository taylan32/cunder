package com.example.cunder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "expire_at")
    private LocalDate expireAt;

    @Column(name = "created_at")
    private LocalDate createdAt;

    public RefreshToken(User user, String token, LocalDate expireAt, LocalDate createdAt) {
        this.user = user;
        this.token = token;
        this.expireAt = expireAt;
        this.createdAt = createdAt;
    }


}
