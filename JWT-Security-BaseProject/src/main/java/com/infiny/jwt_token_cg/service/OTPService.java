package com.infiny.jwt_token_cg.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService mailService;

    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOTP(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        //       ========== old Jwt-Security-base code========
//        message.setTo(email);
//        message.setSubject("Your OTP for Registration");
//        message.setText("Your OTP is: " + otp);
//        mailSender.send(message);

        // new code written for forgot-password project
        mailService.sendEmail(email,"Your OTP for Registration","Your OTP is: " + otp);
    }
}