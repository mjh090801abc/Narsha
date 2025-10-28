package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.UserEntity;
import com.spring.dishcovery.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public int saveUserData(UserEntity user) {

        try {
            String encodedPassword = passwordEncoder.encode(user.getUserPswd());
            user.setUserPswd(encodedPassword);

            return userMapper.saveUserData(user);
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생: {}", e.getMessage(), e);
            return -1;
        }
    }

    public UserEntity getUserData(String userId, String userPswd) {

        UserEntity user = userMapper.findByUserId(userId);

        if(user == null && passwordEncoder.matches(userPswd, user.getUserPswd())) {
            return user;
        }
        return null;
    }



}
