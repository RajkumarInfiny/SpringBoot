package com.infiny.jwt_token_cg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infiny.jwt_token_cg.entities.UnverifiedUser;

public interface UnverifiedUserRepository extends JpaRepository<UnverifiedUser, Long>{

	Optional<UnverifiedUser> findByEmail(String email);

}
