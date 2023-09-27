package com.sparta.miniproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String verificationCode;

    private LocalDateTime expirationTime;

    public EmailVerification(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.expirationTime = LocalDateTime.now().plusMinutes(5);
    }
}
