package com.inventrevo.movieapi.services;

import com.inventrevo.movieapi.entities.RefreshToken;
import com.inventrevo.movieapi.entities.User;
import com.inventrevo.movieapi.repositories.RefereshTokenRepository;
import com.inventrevo.movieapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefereshTokenService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefereshTokenRepository refereshTokenRepository;

    public RefreshToken createRefereshToken(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = user.getRefreshToken();
        if(refreshToken == null){
            long refreshTokenDuration = 5*60*60*10000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(java.util.UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusSeconds(refreshTokenDuration))
                    .user(user)
                    .build();
            refereshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String token){
        RefreshToken refreshToken = refereshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(refreshToken.getExpirationTime().isBefore(Instant.now())){
            refereshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token is expired");
        }
        return refreshToken;
    }
}
