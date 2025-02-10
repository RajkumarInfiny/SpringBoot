package com.infiny.jwt_token_cg.controller;

import com.infiny.jwt_token_cg.dto.ResponseStructure;
import com.infiny.jwt_token_cg.entities.User;
import com.infiny.jwt_token_cg.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Struct;

@RestController
@RequestMapping("/user")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseStructure<?>> forgotPassword(@RequestParam String email){
        passwordResetService.sendResetPasswordEmail(email);
        ResponseStructure<?> structure= new ResponseStructure<>();
        structure.setStatusCode(HttpStatus.ACCEPTED.value());
        structure.setMessage("password reset link send to email");
        structure.setData(null);
        return new ResponseEntity<ResponseStructure<?>>(structure,HttpStatus.ACCEPTED);

    }

    @PostMapping("/reset-password/{resetPasswordToken}")
    public ResponseEntity<ResponseStructure<?>> resetPassword(@PathVariable String resetPasswordToken , @RequestParam String newPassword){
        passwordResetService.resetPassword(resetPasswordToken,newPassword);
        ResponseStructure<?> structure= new ResponseStructure<>();
        structure.setStatusCode(HttpStatus.ACCEPTED.value());
        structure.setMessage("password reset successfully");
        structure.setData(null);
        return new ResponseEntity<ResponseStructure<?>>(structure,HttpStatus.ACCEPTED);

    }

}
