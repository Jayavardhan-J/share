package com.example.Authentication.and.Authorization.with.JWT.transformer;

import com.example.Authentication.and.Authorization.with.JWT.dto.requestDTO.UserRequestDto;
import com.example.Authentication.and.Authorization.with.JWT.dto.responseDTO.UserResponseDto;
import com.example.Authentication.and.Authorization.with.JWT.model.User;

public class UserTransformer {
    public static User UserRequestDto(UserRequestDto userRequestDto){
        return User.builder().name(userRequestDto.getName()).emailId(userRequestDto.getEmailId()).mobile(userRequestDto.getMobile()).build();
    }
    public static UserResponseDto UserToResponseDto(User user){
        return UserResponseDto.builder()
                .name(user.getName())
                .emailId(user.getEmailId())
                .mobile(user.getMobile())
                .build();
    }
}
