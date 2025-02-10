package com.infiny.jwt_token_cg.repository;

import com.infiny.jwt_token_cg.entities.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken,Long> {

    Optional<BlacklistedToken> findByToken(String token);
}
