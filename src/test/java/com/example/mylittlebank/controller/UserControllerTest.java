package com.example.mylittlebank.controller;

import com.example.mylittlebank.api.request.UserRequest;
import com.example.mylittlebank.domain.User;
import com.example.mylittlebank.repository.ImageRepository;
import com.example.mylittlebank.repository.UserRepository;
import com.example.mylittlebank.utils.DataGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void testCreateUser() {
        UserRequest userRequest = DataGenerator.createValidUserRequest();

        mvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

        assertTrue(userRepository.findUserByFullName("test").isPresent());
    }

    @Test
    @SneakyThrows
    void testCreateInvalidUser() {
        UserRequest userRequest = DataGenerator.createInvalidUserRequest();

        mvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void testUpdateUser() {
        User user = DataGenerator.createUser();
        userRepository.save(user);

        UserRequest userRequest = DataGenerator.createUpdateUserRequest();
        mvc.perform(patch("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());
        assertTrue(userRepository.findUserByFullName("test2").isPresent());
    }

    @Test
    @SneakyThrows
    void testGetUserById() {
        User user = DataGenerator.createUser();
        userRepository.save(user);

        mvc.perform(get("/api/users")
                        .param("id", String.valueOf(user.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @SneakyThrows
    void testByUserByFullName() {
        User user = DataGenerator.createUser();
        userRepository.save(user);

        mvc.perform(get("/api/users")
                        .param("fullName", user.getFullName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @SneakyThrows
    void testGetUserByEmail() {
        User user = DataGenerator.createUser();
        userRepository.save(user);

        mvc.perform(get("/api/users")
                        .param("email", user.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @SneakyThrows
    void testAddImage() {
        User user = DataGenerator.createUser();
        userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".getBytes());

        mvc.perform(multipart("/api/users/image")
                        .file(file)
                        .param("id", String.valueOf(user.getId())))
                .andExpect(status().isOk());

        assertEquals(1, imageRepository.count());
    }
}
