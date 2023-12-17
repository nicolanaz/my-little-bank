package com.example.mylittlebank.service;

import com.example.mylittlebank.domain.Image;
import com.example.mylittlebank.domain.User;
import com.example.mylittlebank.exception.UserNotFoundException;
import com.example.mylittlebank.repository.ImageRepository;
import com.example.mylittlebank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Override
    @SneakyThrows
    public void saveImage(Long userId, MultipartFile multipartFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND));

        Image image = new Image();
        image.setUser(user);
        image.setFile(multipartFile.getBytes());

        imageRepository.save(image);
    }
}
