package com.infiny.jwt_token_cg.service;

import com.infiny.jwt_token_cg.entities.Token;
import com.infiny.jwt_token_cg.entities.User;
import com.infiny.jwt_token_cg.repository.TokenRepository;
import com.infiny.jwt_token_cg.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    public String generateToken(String email) {
        //  ==========code for without using Token class=======
//        User user = userRepository.findByEmail(email).get();
//        user.setLogout(false);
//        userRepository.save(user);

        String token= Jwts.builder()
                .setSubject(email)
              //  .claim("isLogout",false) // added claim with default value false at time of creatiion of Token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        User user =userRepository.findByEmail(email).get();
        Token tokenObj = new Token();
        tokenObj.setToken(token);
        tokenObj.setUser(user);
        tokenRepository.save(tokenObj);
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
//        Claims claims = extractClaims(token);
//        boolean isLogout = claims.get("isLogout",Boolean.class);
  //      User user = userRepository.findByEmail(extractUsername(token)).get();
      //  ... && !user.getLogout());
        return (email.equals(extractUsername(token)) && !isTokenExpired(token) && tokenRepository.existsByToken(token));

        //... && tokenRepository.existByToken(token)  new code (By using Token Table)
    }
}
