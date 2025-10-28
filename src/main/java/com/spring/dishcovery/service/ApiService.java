package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.UserEntity;
import com.spring.dishcovery.mapper.ApiMapper;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private BCryptPasswordEncoder  passwordEncoder;

    // 아이디 중복체크
    public int isUserIdExist(String userId) {

        int cnt = 0;
        cnt = apiMapper.countByUserId(userId);

        return cnt;
    }

    public int saveSignupApi(UserEntity userVo) {
        try {

            String encodedPassword = passwordEncoder.encode(userVo.getUserPswd());
            userVo.setUserPswd(encodedPassword);

            return apiMapper.saveSignupApi(userVo);

        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
