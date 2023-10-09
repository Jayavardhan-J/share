package com.example.Authentication.and.Authorization.with.JWT.service;

import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.GetOtpRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.OtpRequestDto;

import com.example.Authentication.and.Authorization.with.JWT.model.OTP;

import com.example.Authentication.and.Authorization.with.JWT.repository.OtpRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;


import java.security.Key;
import java.sql.Time;

import java.util.*;
import java.util.function.Function;

@Service
public class OtpService {
    @Autowired
    OtpRepository otpRepository;

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

        System.out.println(otp);
        String text = "Hello User, \n" + "Your one-time password is : "+otp+"\n"+"Please do not share with anyone. \n"+"Thank You";
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setText(text);
        mail.setTo(oneTimePassword.getEmailId());
        mail.setFrom("auth.otp.generator@gmail.com");
        mail.setSubject("One Time Password");
        mailSender.send(mail);



    }

    public String validateOtp(OtpRequestDto otp) throws Exception {
        OTP savedOtp = otpRepository.findByOneTimePassword(otp.getOneTimePassword());

        if(savedOtp==null || !savedOtp.getEmailId().equals(otp.getEmailId())  ) throw new Exception("Unauthorised Access!!");

        Time start = new Time(savedOtp.getDate().getTime());
        Time end = new Time(new Date().getTime());
        long diff = end.getTime()-start.getTime();

        otpRepository.delete(savedOtp);
        if(diff>5*60*1000) throw new Exception("OTP Expired");


        String token=generateToken(otp.getEmailId());

        return token;
    }

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String email) {
        System.out.println(token + " " +email);
        final String username = extractUsername(token.split(" ")[1].trim());
        return (username.equals(email)) && !isTokenExpired(token.split(" ")[1].trim());
    }
}
