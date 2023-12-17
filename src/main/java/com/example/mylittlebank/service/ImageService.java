package com.example.mylittlebank.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImage(Long id, MultipartFile multipartFile);
}
