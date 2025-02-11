package com.infiny.jwt_token_cg.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.infiny.jwt_token_cg.filter.JwtFilter;
import com.infiny.jwt_token_cg.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;
	@Autowired 
	private AuthenticationConfiguration authenticationConfiguration; 
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
		
	}

	// ======================= old code ==================
//	@Bean
//	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//		return authenticationManagerBuilder.build();
//	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request.requestMatchers("/api/auth/register", "/token/get","/api/auth/login","/api/auth/verify").permitAll()
						// .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
						.requestMatchers("/**").authenticated())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(getAuthenticationProvider())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.authenticationManager(authenticationManager(authenticationConfiguration));
		// .addFilterBefore(new LISTFilter(),BasicAuthenticationFilter.class);
		return http.build();
	}

	 @Bean public AuthenticationProvider getAuthenticationProvider() {
		  DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		                  daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		                  daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		                  return daoAuthenticationProvider; 
		}
//	+++++++++++++++++++++++++++++++++ old Code+++++++++++++++++++++++
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//            .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
//            .anyRequest().authenticated()
//            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
}
