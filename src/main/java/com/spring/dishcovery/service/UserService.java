package com.spring.dishcovery.service;

import com.spring.dishcovery.entity.UserEntity;
import com.spring.dishcovery.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserMapper userMapper;

    private final  PasswordEncoder passwordEncoder;

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


    public void updateProfile(String userId, String userMail, String userName, MultipartFile profileImg,
                              String currentPw, String newPw) throws IOException {

        // 1️⃣ 기본 정보 수정
        userMapper.updateUserInfo(userId, userMail, userName);

        // 2️⃣ 프로필 이미지 변경 (있을 때만)
        if (profileImg != null && !profileImg.isEmpty()) {
            String imgPath = saveProfileImage(userId, profileImg);
            userMapper.updateProfileImage(userId, imgPath);
        }

        // 3️⃣ 비밀번호 변경 (입력했을 때만)
        if (newPw != null && !newPw.isBlank()) {
            String savedPw = userMapper.selectPassword(userId);

            if (!passwordEncoder.matches(currentPw, savedPw)) {
                throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
            }

            userMapper.updatePassword(userId, passwordEncoder.encode(newPw));
        }
    }

//    private String saveProfileImage(String userId, MultipartFile file) throws IOException {
//
//        File dir = new File(profileDir);
//        if (!dir.exists()) dir.mkdirs();
//
//        String ext = file.getOriginalFilename()
//                .substring(file.getOriginalFilename().lastIndexOf("."));
//
//        String fileName = userId + "_" + System.currentTimeMillis() + ext;
//        File saveFile = new File(profileDir + fileName);
//
//        file.transferTo(saveFile);
//
//        return "/profile/" + fileName;
//    }

    public String saveProfileImage(String userId, MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("업로드된 파일이 없습니다.");
        }

        // ✅ 프로젝트 루트 기준
        String projectRoot = System.getProperty("user.dir");

        File dir = new File(
                projectRoot
                        + File.separator + "uploads"
                        + File.separator + "profile"
        );

        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("디렉터리 생성 여부: " + created);
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new RuntimeException("파일명이 올바르지 않습니다.");
        }

        String ext = originalName.substring(originalName.lastIndexOf("."));
        String fileName = userId + "_" + System.currentTimeMillis() + ext;

        File saveFile = new File(dir, fileName);

        System.out.println("저장 경로 = " + saveFile.getAbsolutePath());

        // ✅ 여기서 이제 Tomcat 경로로 안 떨어짐
        file.transferTo(saveFile);

        // DB에는 웹 접근 경로만 저장
        return "/profile/" + fileName;
    }





}
