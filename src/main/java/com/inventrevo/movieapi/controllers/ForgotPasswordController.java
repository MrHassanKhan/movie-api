package com.inventrevo.movieapi.controllers;

import com.inventrevo.movieapi.dto.ChangePasswordDto;
import com.inventrevo.movieapi.dto.MailBody;
import com.inventrevo.movieapi.entities.ForgotPassword;
import com.inventrevo.movieapi.entities.User;
import com.inventrevo.movieapi.repositories.ForgotPasswordRepository;
import com.inventrevo.movieapi.repositories.UserRepository;
import com.inventrevo.movieapi.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyMail(@PathVariable String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        int otp = otpGenerator();
//        String subject = "Hello, ${name}!";
//        String template = "Hello, ${name}!\n\n"
//                + "This is a message just for you, ${name}. "
//                + "We hope you're having a great day!\n\n"
//                + "Your OTP is: ${otp}\n\n"
//                + "Best regards,\n"
//                + "The Spring Boot Team";
//
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("name", user.getName());
        MailBody mailBody = MailBody.builder()
                .to(user.getEmail())
                .subject("OTP for Forgot Password")
                .body("This is the OTP for Forgot Password: " + otp).build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 60 * 1000))
                .user(user)
                .build();
        emailService.sendSimpleMail(mailBody);
        forgotPasswordRepository.save(forgotPassword);
        return ResponseEntity.ok("OTP sent to " + user.getEmail());
    }


    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, user).orElseThrow(() -> new RuntimeException("OTP not found"));
        if (forgotPassword.getExpirationTime().before(new Date())) {
            forgotPasswordRepository.deleteById(forgotPassword.getForgotId());
            return new ResponseEntity<>("OTP expired", HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok("OTP verified");
    }


    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto, @PathVariable String email) {
        if(!Objects.equals(changePasswordDto.password(), changePasswordDto.confirmPassword())) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.EXPECTATION_FAILED);
        }
        String encodedPassword = passwordEncoder.encode(changePasswordDto.password());
        userRepository.updatePassword(email, encodedPassword);
        return ResponseEntity.ok("Password changed successfully");
    }


    private Integer otpGenerator() {
        return new Random().nextInt(100_000, 999_999);
    }
}
