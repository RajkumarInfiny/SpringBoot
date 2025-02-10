package com.infiny.jwt_token_cg.repository;

import com.infiny.jwt_token_cg.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken,Long> {
    Optional<UserToken> findByToken(String token);
    void deleteByUsername(String username);
}
