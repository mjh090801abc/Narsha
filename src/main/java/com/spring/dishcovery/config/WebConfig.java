package com.spring.dishcovery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 프로젝트 루트 폴더 기준 uploads 폴더
        String uploadPath = System.getProperty("user.dir") + "/uploads/";

        // 브라우저 요청 URL: /uploads/**
        // 실제 파일 위치: 프로젝트 루트 /uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);

        System.out.println("Uploads path mapped: " + uploadPath);
    }
}
