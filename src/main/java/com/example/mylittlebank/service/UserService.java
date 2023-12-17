package com.example.mylittlebank.service;

import com.example.mylittlebank.api.request.UserRequest;
import com.example.mylittlebank.api.response.UpdateUserResponse;
import com.example.mylittlebank.api.response.UserResponse;

public interface UserService {
    UserResponse saveUser(UserRequest userRequest);

    UpdateUserResponse updateUser(Long id, UserRequest updateUserRequest);

    UserResponse getById(Long id);

    UserResponse getByFullName(String fullName);

    UserResponse getByEmail(String email);
}
