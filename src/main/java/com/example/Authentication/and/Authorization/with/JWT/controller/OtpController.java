package com.example.Authentication.and.Authorization.with.JWT.controller;

import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.GetOtpRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.OtpRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.service.OtpService;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class OtpController {
    @Autowired
    OtpService otpService;


    @PostMapping("/sendOtp")
    public ResponseEntity generateOTP(@RequestBody GetOtpRequestDto otp){
        otpService.generateOTP(otp);
        return new ResponseEntity("OTP Sent", HttpStatus.CREATED);
    }
    @PostMapping("/validateOtp")
    public ResponseEntity validateOTP(@RequestBody OtpRequestDto otp){
        try{
            String token = otpService.validateOtp(otp);
            return new ResponseEntity(token,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
        }

    }

    }




