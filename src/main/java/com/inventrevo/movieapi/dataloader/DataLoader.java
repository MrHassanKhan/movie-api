package com.inventrevo.movieapi.dataloader;

import com.inventrevo.movieapi.entities.Movie;
import com.inventrevo.movieapi.entities.User;
import com.inventrevo.movieapi.entities.UserRole;
import com.inventrevo.movieapi.repositories.MovieRepository;
import com.inventrevo.movieapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(userRepository.count() == 0) {
            var user = User.builder()
                    .username("admin")
                    .email("muhammadhsn09@gmail.com")
                    .name("Muhammad Hsn")
                    .password(passwordEncoder.encode("admin"))
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(user);
            var user2 = User.builder()
                    .username("user")
                    .email("muhammad.hassan.app@gmail.com")
                    .name("Muhammad Hsn")
                    .password(passwordEncoder.encode("user"))
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user2);
        }
    }
}
