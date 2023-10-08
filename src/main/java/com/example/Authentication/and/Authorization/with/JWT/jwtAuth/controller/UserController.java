package com.example.Authentication.and.Authorization.with.JWT.jwtAuth.controller;



import com.example.Authentication.and.Authorization.with.JWT.jwtAuth.entity.AuthRequest;
import com.example.Authentication.and.Authorization.with.JWT.jwtAuth.entity.UserInfo;
import com.example.Authentication.and.Authorization.with.JWT.jwtAuth.service.UserInfoService;
import com.example.Authentication.and.Authorization.with.JWT.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @GetMapping("/welcome")
//    public String welcome() {
//        return "Welcome this endpoint is not secure";
//    }
//
    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }
//
//    @GetMapping("/user/userProfile")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public String userProfile() {
//        return "Welcome to User Profile";
//    }
//
//    @GetMapping("/admin/adminProfile")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public String adminProfile() {
//        return "Welcome to Admin Profile";
//    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        // username and password
        System.out.println(authRequest.getEmail() +" "+authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

    }

}