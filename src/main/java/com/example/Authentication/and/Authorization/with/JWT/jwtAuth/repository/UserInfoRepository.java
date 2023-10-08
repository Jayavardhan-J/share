package com.example.Authentication.and.Authorization.with.JWT.jwtAuth.repository;



import com.example.Authentication.and.Authorization.with.JWT.jwtAuth.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);
    Optional<UserInfo> findByEmail(String email);
}
