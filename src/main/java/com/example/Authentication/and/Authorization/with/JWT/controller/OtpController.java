package com.example.Authentication.and.Authorization.with.JWT.controller;

import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.GetOtpRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.OtpRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class OtpController {
    @Autowired
    UserService userService;


    @PostMapping("/sendOtp")
    public ResponseEntity generateOTP(@RequestBody GetOtpRequestDto otp){
//        try{
//
//            String url="http://localhost:8081/user/test";
//            RestTemplate restTemplate = new RestTemplate();
//            String result = restTemplate.getForObject(url, String.class);
//            System.out.println(result);
//        }
//        catch(Exception e){
//            System.out.println(e.getMessage());
//        }

            userService.generateOTP(otp);
            return new ResponseEntity("OTP Sent", HttpStatus.CREATED);
    }
    @PostMapping("/validateOtp")
    public ResponseEntity validateOTP(@RequestBody OtpRequestDto otp){
        try{
            return userService.validateOtp(otp);
//            return new ResponseEntity(object,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
        }

    }



}
