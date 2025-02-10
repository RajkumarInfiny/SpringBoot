package com.infiny.jwt_token_cg.service;

import com.infiny.jwt_token_cg.entities.BlacklistedToken;
import com.infiny.jwt_token_cg.entities.UserToken;
import com.infiny.jwt_token_cg.repository.BlacklistedTokenRepository;
import com.infiny.jwt_token_cg.repository.UserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    @Autowired
    UserTokenRepository userTokenRepository;

    @Autowired
    BlacklistedTokenRepository blacklistedTokenRepository;

    public String generateToken(String email) {

        String token= Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        userTokenRepository.deleteByUsername(email);
        userTokenRepository.save(new UserToken(token,email,LocalDateTime.now()));

        return token;
    }

   public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
//    public Claims extractClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)  // ✅ Use `parserBuilder()` instead of `parser()`
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String email) {
        Optional<BlacklistedToken> blacklistedToken = blacklistedTokenRepository.findByToken(token);

        return (email.equals(extractUsername(token)) && !isTokenExpired(token) && blacklistedToken.isEmpty());
    }
}
