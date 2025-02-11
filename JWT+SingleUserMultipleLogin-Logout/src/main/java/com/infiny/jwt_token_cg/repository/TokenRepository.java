package com.infiny.jwt_token_cg.repository;

import com.infiny.jwt_token_cg.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByToken(String token);
    boolean existsByToken(String token);

    void deleteByToken(String token);
}
