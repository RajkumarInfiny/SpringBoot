package com.infiny.jwt_token_cg.filter;

import com.infiny.jwt_token_cg.entities.BlacklistedToken;
import com.infiny.jwt_token_cg.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.infiny.jwt_token_cg.service.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
@WebFilter
public class JwtFilter extends OncePerRequestFilter {

	//UsernamePasswordAuthenticationFilter
    @Autowired
    private JwtUtil jwtUtil;

	@Autowired
	BlacklistedTokenRepository blacklistedTokenRepository;

//    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ");
    }

    
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
       String header = request.getHeader("Authorization");
//        String token = header.substring(7);
//        String username = jwtUtil.extractUsername(token);
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Validate token
//            if (jwtUtil.validateToken(token, username)) {
//                SecurityContextHolder.getContext().setAuthentication(
//                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()));
//            }
//        }
    	
    	 if (header != null && header.startsWith("Bearer ")) {
    	        String token = header.substring(7); // Extract the token
    	        String username = jwtUtil.extractUsername(token);

			 Optional<BlacklistedToken> blacklistedToken = blacklistedTokenRepository.findByToken(token);
			 if(blacklistedToken.isPresent()){
				 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid (Logged out)");
				 return;
			 }
    	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    	            // Validate token
    	            if (jwtUtil.validateToken(token, username)) {
    	                SecurityContextHolder.getContext().setAuthentication(
    	                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()));
    	            }
    	        }
    	    }

        chain.doFilter(request, response);
    }
}
