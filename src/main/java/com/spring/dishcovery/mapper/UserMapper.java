package com.spring.dishcovery.mapper;

import com.spring.dishcovery.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    public int saveUserData(UserEntity user);

    public UserEntity findByUserId(String userId);

    public List<UserEntity> findRecommUser(String userId);

    public void updateProfile(UserEntity user);

    public void updateProfileWithoutPwd(UserEntity user);

    public String findPasswordByUserId(String userId);

    public void updateUserInfo(String userId, String userMail, String userName);

    public void updateProfileImage(String userId, String userImgPath);

    public String selectPassword(String userId);

    public void updatePassword(String userId, String newPw);

}


