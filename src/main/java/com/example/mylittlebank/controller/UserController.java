package com.example.mylittlebank.controller;

import com.example.mylittlebank.api.request.UserRequest;
import com.example.mylittlebank.api.response.UpdateUserResponse;
import com.example.mylittlebank.api.response.UserResponse;
import com.example.mylittlebank.service.ImageService;
import com.example.mylittlebank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Validated UserRequest userRequest) {
        return ResponseEntity.ok(userService.saveUser(userRequest));
    }

    @GetMapping
    public ResponseEntity<UserResponse> findUser(@RequestParam(name = "id", required = false) Long id,
                                                 @RequestParam(name = "fullName", required = false) String fullName,
                                                 @RequestParam(name = "email", required = false) String email) {
        if (id != null) {
            return ResponseEntity.ok(userService.getById(id));
        } else if (fullName != null) {
            return ResponseEntity.ok(userService.getByFullName(fullName));
        } else if (email != null) {
            return ResponseEntity.ok(userService.getByEmail(email));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> update(@PathVariable("id") Long id, @RequestBody UserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }

    @PostMapping("/image")
    public ResponseEntity<?> addImage(@RequestParam("id") Long id, @RequestPart("file") MultipartFile multipartFile) {
        imageService.saveImage(id, multipartFile);
        return ResponseEntity.ok().build();
    }
}
