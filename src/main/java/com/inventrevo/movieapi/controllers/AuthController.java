package com.inventrevo.movieapi.controllers;

import com.inventrevo.movieapi.dto.auth.AuthResponse;
import com.inventrevo.movieapi.dto.auth.LoginRequest;
import com.inventrevo.movieapi.dto.auth.RefreshTokenRequest;
import com.inventrevo.movieapi.dto.auth.RegisterRequest;
import com.inventrevo.movieapi.entities.RefreshToken;
import com.inventrevo.movieapi.services.AuthService;
import com.inventrevo.movieapi.services.RefereshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RefereshTokenService refereshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok().body(authService.register(registerRequest));
    }


    @PostMapping("/refresh")
    public ResponseEntity<RefreshToken> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok().body(refereshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken()));
    }

}
