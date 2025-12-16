package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.UserEntity;
import com.spring.dishcovery.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        if(user != null && passwordEncoder.matches(userPswd, user.getUserPswd())) {
            return user;
        }
        return null;
    }

    public UserEntity findByUserId(String userId) {
        return  userMapper.findByUserId(userId);
    }

    public List<UserEntity> findRecommUser(String userId) {
        return userMapper.findRecommUser(userId);

    }

    @Transactional
    public boolean changePassword(UserEntity user) {

        boolean result = false;

        if ((user.getCurrentPw() == null || user.getCurrentPw().trim().isEmpty()) && (user.getNewPw() == null || user.getNewPw().trim().isEmpty()) ){
            // 비밀번호가 없을 때 처리
            userMapper.updateProfileWithoutPwd(user);

            result = true;

        }else{

            String userPswd = userMapper.findPasswordByUserId(user.getUserId());

            // 기존 비밀번호 확인
            if (!passwordEncoder.matches(user.getCurrentPw(), userPswd)) {
                result = false;
            }
            // 새 비밀번호 암호화
            String newEncodedPw = passwordEncoder.encode(user.getNewPw());
            user.setNewPw(newEncodedPw);

            userMapper.updateProfile(user);

            result = true;

        }

        return result;
    }




}
