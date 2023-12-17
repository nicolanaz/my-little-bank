package com.example.mylittlebank.controller;

import com.example.mylittlebank.api.request.AccountRequest;
import com.example.mylittlebank.domain.Account;
import com.example.mylittlebank.domain.User;
import com.example.mylittlebank.repository.AccountRepository;
import com.example.mylittlebank.repository.UserRepository;
import com.example.mylittlebank.utils.DataGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends BaseControllerTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        accountRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void testCreateAccount() {
        User user = DataGenerator.createUser();
        userRepository.save(user);

        AccountRequest accountRequest = DataGenerator.createAccountRequest();

        mvc.perform(post("/api/accounts/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isOk());

        assertFalse(accountRepository.findAll().isEmpty());
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void testDeleteAccount() {
        Account account = DataGenerator.createAccount();
        accountRepository.save(account);

        mvc.perform(delete("/api/accounts/" + account.getAccountNumber()))
                .andExpect(status().isOk());

        assertTrue(accountRepository.findAll().isEmpty());
    }
}
