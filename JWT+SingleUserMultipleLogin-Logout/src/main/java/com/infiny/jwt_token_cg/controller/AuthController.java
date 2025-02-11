package com.infiny.jwt_token_cg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.infiny.jwt_token_cg.dto.ResponseStructure;
import com.infiny.jwt_token_cg.entities.User;
import com.infiny.jwt_token_cg.service.JwtUtil;
import com.infiny.jwt_token_cg.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	//===============Old Working code================
	// not secured
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody User user) {
//        userService.registerUser(user.getName(), user.getEmail(), user.getPhoneNumber(), user.getPassword());
//        return ResponseEntity.ok("User registered successfully");
//    }  

	
	//      code  from two table otp verification
	
	
	//           Not Secured
	@PostMapping("/register")
	public ResponseEntity<ResponseStructure<String>> registerUser(@RequestBody User user) {
		userService.registerUser(user.getName(), user.getEmail(), user.getPhoneNumber(),user.getPassword());
	    ResponseStructure<String> structure =new ResponseStructure<String>();		
		structure.setStatusCode(HttpStatus.ACCEPTED.value());
		structure.setMessage("OTP is sent to your email");
		structure.setData(null);
		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.ACCEPTED);
	}

	//code from two table otp verification
	//    not secured
	@PostMapping("/verify")
	public ResponseEntity<ResponseStructure<User>> verifyOTP(@RequestParam String email,
			@RequestParam String otp) {
		ResponseStructure<User> structure=new ResponseStructure<User>();
		if (userService.verifyOTP(email, otp)) {

			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			structure.setMessage("ACCEPTED");
			structure.setData(null);
			return new ResponseEntity<ResponseStructure<User>>(structure,HttpStatus.ACCEPTED);
		} else {
			structure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
			structure.setMessage("NOT ACCEPTABLE");
			structure.setData(null);
			return new ResponseEntity<ResponseStructure<User>>(structure,HttpStatus.NOT_ACCEPTABLE);
		
		}
	}
	
	// implemented in JwtController
	@GetMapping("/login")
	public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
		// Here, you would validate the password and if correct, generate JWT
		String token = jwtUtil.generateToken(email);
		return ResponseEntity.ok("Bearer " + token);
	}

	//secured
	@GetMapping("/allUsers")
	public ResponseEntity<ResponseStructure<List<User>>> getAllUsers() {
		List<User> userList = userService.getAllUsers();
		ResponseStructure<List<User>> structure=new ResponseStructure<List<User>>();
		if(!(userList.equals(null))) 
		{
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			structure.setMessage("ACCEPTED");
			structure.setData(userList);
		return new ResponseEntity<ResponseStructure<List<User>>>(structure,HttpStatus.ACCEPTED);
		}else {
			structure.setStatusCode(HttpStatus.NO_CONTENT.value());
			structure.setMessage("DATA NOT AVAILABLE");
			structure.setData(null);
			return new ResponseEntity<ResponseStructure<List<User>>>(structure,HttpStatus.NO_CONTENT);
		}
	}
	
	//secured
	@PutMapping("/user")
	public ResponseEntity<ResponseStructure<User>> updateUser(@RequestBody User user){
		ResponseStructure<User> structure=new ResponseStructure<User>();
		User updatedUser=userService.updateUser(user);
		structure.setStatusCode(HttpStatus.ACCEPTED.value());
		structure.setMessage("Updated Sucessfully");
		structure.setData(updatedUser);
		return new ResponseEntity<ResponseStructure<User>>(structure,HttpStatus.ACCEPTED);
		
	}
	
     @GetMapping("/user/{id}")
     public ResponseEntity<ResponseStructure<User>> getuserById(@RequestParam Long id){
 		ResponseStructure<User> structure=new ResponseStructure<User>();
 		User updatedUser=userService.getUserById(id);
 		structure.setStatusCode(HttpStatus.FOUND.value());
 		structure.setMessage("User Found");
 		structure.setData(updatedUser);
 		return new ResponseEntity<ResponseStructure<User>>(structure,HttpStatus.FOUND);
 		
 	}
     
 //    @PostMapping("/user/logout")
//	public ResponseEntity<ResponseStructure<?>> logout(@RequestParam String email){
//		userService.logout(email);
//		 ResponseStructure<?> structure=new ResponseStructure<User>();
//		 structure.setStatusCode(HttpStatus.OK.value());
//		 structure.setMessage("Logout Successful");
//		 structure.setData(null);
//		 return new ResponseEntity<ResponseStructure<?>>(structure,HttpStatus.OK);
//	 }

	@PostMapping("/user/logout")
	public ResponseEntity<ResponseStructure<?>> logout(@RequestHeader("Authorization") String header,@RequestParam String email){
		userService.logout(header,email);
		ResponseStructure<?> structure=new ResponseStructure<User>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Logout Successful");
		structure.setData(null);
		return new ResponseEntity<ResponseStructure<?>>(structure,HttpStatus.OK);
	}
     
}
