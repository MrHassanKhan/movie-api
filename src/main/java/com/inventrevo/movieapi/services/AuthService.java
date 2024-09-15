package com.inventrevo.movieapi.services;

import com.inventrevo.movieapi.dto.auth.AuthResponse;
import com.inventrevo.movieapi.dto.auth.LoginRequest;
import com.inventrevo.movieapi.dto.auth.RegisterRequest;
import com.inventrevo.movieapi.entities.User;
import com.inventrevo.movieapi.entities.UserRole;
import com.inventrevo.movieapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefereshTokenService refereshTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Username already taken");
        }
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refereshTokenService.createRefereshToken(savedUser.getUsername());
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refereshTokenService.createRefereshToken(user.getUsername());
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }
}
