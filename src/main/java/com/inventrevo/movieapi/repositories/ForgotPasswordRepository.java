package com.inventrevo.movieapi.repositories;

import com.inventrevo.movieapi.entities.ForgotPassword;
import com.inventrevo.movieapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    ForgotPassword findByOtp(Integer otp);
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

}
