package com.infiny.jwt_token_cg.service;

import com.infiny.jwt_token_cg.entities.PasswordResetToken;
import com.infiny.jwt_token_cg.entities.User;
import com.infiny.jwt_token_cg.repository.PasswordResetTokenRepository;
import com.infiny.jwt_token_cg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    // Generate Token and Send Email
    public void sendResetPasswordEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = UUID.randomUUID().toString(); // Generate random token
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15); // 15 min expiry

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
        passwordResetTokenRepository.save(resetToken);

        // Send Email with Reset Link
        String resetLink = "http://localhost:8080/user/reset-password/" + token+"/";
        emailService.sendEmail(user.getEmail(), "Password Reset Request",
                "Click the link to reset your password: " + resetLink);
    }

    // Validate Token and Reset Password
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        // Update User Password
        User user = resetToken.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        // Delete Token after successful reset
        passwordResetTokenRepository.delete(resetToken);
    }
}

