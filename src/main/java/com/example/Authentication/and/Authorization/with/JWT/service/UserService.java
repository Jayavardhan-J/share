package com.example.Authentication.and.Authorization.with.JWT.service;

import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.GetOtpRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.OtpRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.UserRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.jwtAuth.entity.UserInfo;
import com.example.Authentication.and.Authorization.with.JWT.jwtAuth.repository.UserInfoRepository;
import com.example.Authentication.and.Authorization.with.JWT.model.OTP;
import com.example.Authentication.and.Authorization.with.JWT.model.User;
import com.example.Authentication.and.Authorization.with.JWT.repository.OtpRepository;
import com.example.Authentication.and.Authorization.with.JWT.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    OtpRepository otpRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    JavaMailSender mailSender;
    public void generateOTP(GetOtpRequestDto oneTimePassword) {
        int otp =0;
        while(true){
            Random random = new Random();
            int randomOtp = random.nextInt(900000)+100000;
            if(otpRepository.findByOneTimePassword(randomOtp)==null){
                otp=randomOtp;
                break;
            }
        }
        OTP saveOtp = new OTP();
        saveOtp.setDate(new Date());
        saveOtp.setEmailId(oneTimePassword.getEmailId());
        saveOtp.setOneTimePassword(otp);
        otpRepository.save(saveOtp);
        //save as password of the user with otp
        UserInfo user = userInfoRepository.findByEmail(oneTimePassword.getEmailId()).get();
        if(user != null){
            user.setPassword(String.valueOf(otp));
        } else {
            user = new UserInfo();
            user.setEmail(oneTimePassword.getEmailId());
            user.setPassword(String.valueOf(otp));
        }
        userInfoRepository.save(user);


        String text = "Hello User, \n" + "Your one-time password is : "+otp+"\n"+"Please do not share with anyone. \n"+"Thank You";
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setText(text);
        mail.setTo(oneTimePassword.getEmailId());
        mail.setFrom("auth.otp.generator@gmail.com");
        mail.setSubject("One Time Password");
        mailSender.send(mail);



    }

    public ResponseEntity validateOtp(OtpRequestDto otp) throws Exception {
        OTP savedOtp = otpRepository.findByOneTimePassword(otp.getOneTimePassword());

        if(savedOtp==null || !savedOtp.getEmailId().equals(otp.getEmailId())  ) throw new Exception("Unauthorised Access!!");

        Time start = new Time(savedOtp.getDate().getTime());
        Time end = new Time(new Date().getTime());
        long diff = end.getTime()-start.getTime();
        System.out.println(diff);
//        otpRepository.delete(savedOtp);
        if(diff>5*60*1000) throw new Exception("OTP Expired");

//        String jsonBody =String.format("{\"email\":\"%s\" , \"password\": %d }",otp.getEmailId(),otp.getOneTimePassword());
        String jsonBody = "{\"email\":\"Jay\" , \"password\": 581838}";
        System.out.print(jsonBody);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate template = new RestTemplate();
        String url ="http://localhost:8081/auth/generateToken";

        HttpEntity<String> request = new HttpEntity<>(jsonBody,header);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST,request,String.class);
        System.out.println(response);
        return response;
    }
}
