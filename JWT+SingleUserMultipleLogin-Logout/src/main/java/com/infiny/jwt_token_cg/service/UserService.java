package com.infiny.jwt_token_cg.service;

import java.util.List;
import java.util.Optional;

import com.infiny.jwt_token_cg.repository.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.infiny.jwt_token_cg.entities.UnverifiedUser;
import com.infiny.jwt_token_cg.entities.User;
import com.infiny.jwt_token_cg.repository.UnverifiedUserRepository;
import com.infiny.jwt_token_cg.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UnverifiedUserRepository unverifiedUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	// private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private OTPService otpService;

	@Autowired
	private TokenRepository tokenRepository;
	/*
	 * 
	 * -----------------------old working code --------------
	 * 
	 * // Register user public void registerUser(String name, String email, String
	 * phoneNumber, String password) { User user = new User(); user.setName(name);
	 * user.setEmail(email); user.setPhoneNumber(phoneNumber); //
	 * user.setPassword(password);
	 * user.setPassword(passwordEncoder.encode(password)); // Encrypt the password
	 * before saving user.setVerified(false); // Default to false, but you can later
	 * add OTP verification userRepository.save(user); }
	 * 
	 * ------------------------------------------------
	 */

	// code from two table otp start
	public void registerUser(String name, String email, String phoneNumber, String password) {
		Optional<User> existingUser = userRepository.findByEmail(email);
		Optional<UnverifiedUser> existingUnVerifiedser = unverifiedUserRepository.findByEmail(email);
		UnverifiedUser unverifiedUser = new UnverifiedUser();
		String otp = otpService.generateOTP();

		if (existingUser.isPresent()) {
			throw new RuntimeException("User with this email already exists!");
		}

		if (existingUnVerifiedser.isPresent()) {
			unverifiedUser = existingUnVerifiedser.get();
			unverifiedUser.setOtp(otp);
		} else {
			unverifiedUser.setName(name);
			unverifiedUser.setEmail(email);
			unverifiedUser.setPhoneNumber(phoneNumber);
			// unverifiedUser.setPassword(password);
			unverifiedUser.setPassword(passwordEncoder.encode(password));
			unverifiedUser.setOtp(otp);

		}
		unverifiedUserRepository.save(unverifiedUser);

		otpService.sendOTP(email, otp);
	}

	public boolean verifyOTP(String email, String otp) {
		UnverifiedUser unverifiedUser = unverifiedUserRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));
		if (unverifiedUser.getOtp().equals(otp)) {
			User User = new User();
			User.setEmail(email);
			User.setPassword(unverifiedUser.getPassword());
			User.setName(unverifiedUser.getName());
			User.setPhoneNumber(unverifiedUser.getPhoneNumber());
			userRepository.save(User);
			unverifiedUserRepository.delete(unverifiedUser);
			return true;
		}
		return false;
	}

	// code from two table otp end

	// Fetch user by email (for login)
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public List<User> getAllUsers() {

		return userRepository.findAll();
	}

	public User updateUser(User user) {
		if (user.getId() == 0) {
			throw new IllegalArgumentException("User ID can't be Null");
		}
		if (!userRepository.existsById(user.getId())) {
			throw new RuntimeException("User not found");
		}

		return userRepository.save(user);

	}

	public User getUserById(Long id) {
		
		return userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("User not Found"));
	}

	@Transactional
	public boolean logout(String header,String email){
		User user = userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("Username not found"));
		String token = getTokenFromHeader(header);
		tokenRepository.deleteByToken(token);
//		user.setLogout(true);
//		userRepository.save(user);
		return true;
	}
	public String getTokenFromHeader(String header) throws RuntimeException {
		if(header.startsWith("Bearer ")){
			return header.substring(7);
		}
		 throw new RuntimeException("Invalid Authorization header");
	}
}
