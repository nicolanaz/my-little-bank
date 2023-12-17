package com.example.mylittlebank.utils;

import com.example.mylittlebank.api.request.AccountRequest;
import com.example.mylittlebank.api.request.PeriodTransactionsRequest;
import com.example.mylittlebank.api.request.TransactionRequest;
import com.example.mylittlebank.api.request.UserRequest;
import com.example.mylittlebank.domain.Account;
import com.example.mylittlebank.domain.User;

import java.time.LocalDate;
import java.util.Collections;

public class DataGenerator {

    public static UserRequest createValidUserRequest() {
        return UserRequest.builder()
                .fullName("test")
                .email("test@test.ru")
                .phone("+79999999999")
                .address("test")
                .dateOfBirth(LocalDate.now())
                .build();
    }

    public static UserRequest createInvalidUserRequest() {
        return UserRequest.builder()
                .fullName("test")
                .email("test")
                .phone("test")
                .address("test")
                .dateOfBirth(LocalDate.now())
                .build();
    }

    public static UserRequest createUpdateUserRequest() {
        return UserRequest.builder()
                .fullName("test2")
                .email("test2@test.ru")
                .phone("+79888888888")
                .address("test2")
                .dateOfBirth(LocalDate.now())
                .build();
    }

    public static User createUser() {
        return User.builder()
                .fullName("test")
                .email("test@test.ru")
                .phone("+79999999999")
                .address("test")
                .dateOfBirth(LocalDate.now())
                .build();
    }

    public static AccountRequest createAccountRequest() {
        return new AccountRequest(365);
    }

    public static Account createAccount() {
        return Account.builder()
                .openingDate(LocalDate.now())
                .validityPeriod(365)
                .transactions(Collections.emptySet())
                .build();
    }

    public static TransactionRequest createTransactionRequest() {
        return new TransactionRequest();
    }

    public static PeriodTransactionsRequest createPeriodTransactionsRequest() {
        return PeriodTransactionsRequest.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();
    }
}
