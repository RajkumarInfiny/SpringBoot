package com.infiny.jwt_token_cg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.infiny.jwt_token_cg.dto.ResponseStructure;

@ControllerAdvice
public class GlobalExceptionHadler {

	@ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> handleRuntimeException(Exception ex) {
    	ResponseStructure<String> structure = new ResponseStructure<String>();
    	structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
    	structure.setMessage(ex.getMessage());
    	structure.setData(null);
        return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.BAD_REQUEST);
        }
	
	@ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
    	ResponseStructure<String> structure = new ResponseStructure<String>();
    	structure.setStatusCode(HttpStatus.UNAUTHORIZED.value());
    	structure.setMessage(ex.getMessage());
    	structure.setData(null);
        return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.UNAUTHORIZED);
        }
	@ExceptionHandler(IllegalArgumentException.class)
	 public ResponseEntity<ResponseStructure<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
    	ResponseStructure<String> structure = new ResponseStructure<String>();
    	structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
    	structure.setMessage(ex.getMessage());
    	structure.setData(null);
        return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.BAD_REQUEST);
        }

}
