package com.spring.dishcovery.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserEntity {

    private String userMail;

    private String userName;

    private String userId;

    private String userPswd;

    private String userImgPath;

}
