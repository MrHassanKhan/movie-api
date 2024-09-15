package com.inventrevo.movieapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(nullable = false)
    @NotBlank(message = "Please provide refresh token!")
    private String refreshToken;

    @Column(nullable = false)
    private Instant expirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
