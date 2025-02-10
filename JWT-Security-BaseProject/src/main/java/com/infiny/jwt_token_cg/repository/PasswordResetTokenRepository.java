package com.infiny.jwt_token_cg.repository;

import com.infiny.jwt_token_cg.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken ,Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
