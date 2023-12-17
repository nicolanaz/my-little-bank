package com.example.mylittlebank.service;

import com.example.mylittlebank.api.request.UserRequest;
import com.example.mylittlebank.api.response.UpdateUserResponse;
import com.example.mylittlebank.api.response.UserResponse;
import com.example.mylittlebank.domain.User;
import com.example.mylittlebank.exception.UserNotFoundException;
import com.example.mylittlebank.mapper.UserMapper;
import com.example.mylittlebank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse saveUser(UserRequest userRequest) {
        User user = userMapper.mapToUser(userRequest);
        userRepository.save(user);
        return userMapper.mapToResponse(user);
    }

    @Override
    @Transactional
    public UpdateUserResponse updateUser(Long userId, UserRequest updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND));

        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

        userRepository.save(existingUser);
        return userMapper.mapToUpdateResponse(existingUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND));
        return userMapper.mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByFullName(String fullName) {
        User user = userRepository.findUserByFullName(fullName)
                .orElseThrow(() -> new UserNotFoundException("User not found with full name: " + fullName, HttpStatus.NOT_FOUND));
        return userMapper.mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email, HttpStatus.NOT_FOUND));
        return userMapper.mapToResponse(user);
    }
}
