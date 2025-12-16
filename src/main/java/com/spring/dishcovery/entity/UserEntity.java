package com.spring.dishcovery.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UserEntity {

    private String userMail;
    private String userName;
    private String userId;
    private String userPswd;
    private String userImgPath;

    private String currentPw;
    private String newPw;
    private String newPwCheck;
   // private String profileImg;

    List<MultipartFile> profileImg;;

}
