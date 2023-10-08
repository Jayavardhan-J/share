package com.example.Authentication.and.Authorization.with.JWT.repository;

import com.example.Authentication.and.Authorization.with.JWT.model.OTP;
import com.example.Authentication.and.Authorization.with.JWT.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<OTP,String> {
    public OTP findByEmailId(String emailId);
    public OTP findByOneTimePassword(int otp);
}
