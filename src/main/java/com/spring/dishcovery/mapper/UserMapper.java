package com.spring.dishcovery.mapper;

import com.spring.dishcovery.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public int saveUserData(UserEntity user);

    public UserEntity findByUserId(String userId);
}


