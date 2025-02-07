package com.infiny.jwt_token_cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infiny.jwt_token_cg.dto.JwtRequest;
import com.infiny.jwt_token_cg.dto.JwtResponse;
import com.infiny.jwt_token_cg.service.CustomUserDetailsService;
import com.infiny.jwt_token_cg.service.JwtUtil;

@RestController
@RequestMapping("/token")
public class JwtController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	JwtUtil jwtUtil;

	@PostMapping("/get")
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) {

		try {

			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException("Bad Credentials");
		}

		// code to generate token
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

		String token = this.jwtUtil.generateToken(jwtRequest.getUsername());

		return ResponseEntity.ok(new JwtResponse(token));
	}
}
