package com.example.mylittlebank.controller;

import com.example.mylittlebank.api.request.PeriodTransactionsRequest;
import com.example.mylittlebank.api.request.TransactionRequest;
import com.example.mylittlebank.api.response.TransactionResponse;
import com.example.mylittlebank.domain.Account;
import com.example.mylittlebank.domain.TransactionType;
import com.example.mylittlebank.domain.User;
import com.example.mylittlebank.repository.AccountRepository;
import com.example.mylittlebank.repository.TransactionRepository;
import com.example.mylittlebank.repository.UserRepository;
import com.example.mylittlebank.utils.DataGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest extends BaseControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void clean() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void depositTest() {
        Account account = DataGenerator.createAccount();
        accountRepository.save(account);

        TransactionRequest transactionRequest = DataGenerator.createTransactionRequest();
        transactionRequest.setAccountNumber(account.getAccountNumber());
        transactionRequest.setType(TransactionType.DEPOSIT);
        transactionRequest.setAmount(1000);

        int concurrentRequests = 5;

        List<Callable<TransactionResponse>> depositTasks = getCallables(concurrentRequests, post("/api/transactions/deposit"), transactionRequest);

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentRequests);
        List<Future<TransactionResponse>> futures = executorService.invokeAll(depositTasks);

        for (Future<TransactionResponse> future : futures) {
            TransactionResponse actualResponse = future.get();
        }

        executorService.shutdown();

        Account updatedAccount = accountRepository.findById(account.getAccountNumber()).get();
        assertEquals(5000, (int) updatedAccount.getAmount());
    }

    @Test
    @SneakyThrows
    void withdrawTest() {
        Account account = DataGenerator.createAccount();
        account.setAmount(10000);
        accountRepository.save(account);

        TransactionRequest transactionRequest = DataGenerator.createTransactionRequest();
        transactionRequest.setAccountNumber(account.getAccountNumber());
        transactionRequest.setType(TransactionType.WITHDRAW);
        transactionRequest.setAmount(1000);

        int concurrentRequests = 5;

        List<Callable<TransactionResponse>> depositTasks = getCallables(concurrentRequests, post("/api/transactions/withdraw"), transactionRequest);

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentRequests);
        List<Future<TransactionResponse>> futures = executorService.invokeAll(depositTasks);

        for (Future<TransactionResponse> future : futures) {
            TransactionResponse actualResponse = future.get();
        }

        executorService.shutdown();

        Account updatedAccount = accountRepository.findById(account.getAccountNumber()).get();
        assertEquals(5000, (int) updatedAccount.getAmount());
    }

    @Test
    @SneakyThrows
    void transferTest() {
        Account firstAccount = DataGenerator.createAccount();
        firstAccount.setAmount(10000);
        accountRepository.save(firstAccount);

        TransactionRequest firstTransactionRequest = DataGenerator.createTransactionRequest();
        firstTransactionRequest.setAccountNumber(firstAccount.getAccountNumber());
        firstTransactionRequest.setType(TransactionType.TRANSFER);
        firstTransactionRequest.setAmount(1000);

        Account secondAccount = DataGenerator.createAccount();
        secondAccount.setAmount(10000);
        accountRepository.save(secondAccount);

        TransactionRequest secondTransactionRequest = DataGenerator.createTransactionRequest();
        secondTransactionRequest.setAccountNumber(secondAccount.getAccountNumber());
        secondTransactionRequest.setType(TransactionType.TRANSFER);
        secondTransactionRequest.setAmount(1000);

        int concurrentRequests = 2;

        List<Callable<TransactionResponse>> depositTasks1 = getCallables(concurrentRequests, post("/api/transactions/transfer")
                .param("to", String.valueOf(secondAccount.getAccountNumber())), firstTransactionRequest);

        List<Callable<TransactionResponse>> depositTasks2 = getCallables(concurrentRequests / 2, post("/api/transactions/transfer")
                .param("to", String.valueOf(firstAccount.getAccountNumber())), secondTransactionRequest);

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentRequests);
        List<Future<TransactionResponse>> futures1 = executorService.invokeAll(depositTasks1);
        List<Future<TransactionResponse>> futures2 = executorService.invokeAll(depositTasks2);

        for (Future<TransactionResponse> future : futures1) {
            TransactionResponse actualResponse = future.get();
        }

        for (Future<TransactionResponse> future : futures2) {
            TransactionResponse actualResponse = future.get();
        }

        executorService.shutdown();

        assertEquals(10000, firstAccount.getAmount());
        assertEquals(10000, secondAccount.getAmount());
    }

    @Test
    @SneakyThrows
    void testTransferMoney() {
        Account account = DataGenerator.createAccount();

        User user = DataGenerator.createUser();
        user.setAccounts(Set.of(account));
        userRepository.save(user);

        TransactionRequest transactionRequest = DataGenerator.createTransactionRequest();
        transactionRequest.setAccountNumber(account.getAccountNumber());
        transactionRequest.setType(TransactionType.DEPOSIT);
        transactionRequest.setAmount(1000);

        for (int i = 0; i < 5; i++) {
            mvc.perform(post("/api/transactions/deposit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionRequest)))
                    .andExpect(status().isOk());
        }

        PeriodTransactionsRequest periodTransactionsRequest = DataGenerator.createPeriodTransactionsRequest();
        periodTransactionsRequest.setUserId(user.getId());

        MvcResult result = mvc.perform(post("/api/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(periodTransactionsRequest)))
                .andExpect(status().isOk())
                .andReturn();

        List<TransactionResponse> responseList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<TransactionResponse>>() {});
        assertEquals(5, responseList.size());
    }

    private List<Callable<TransactionResponse>> getCallables(int concurrentRequests, MockHttpServletRequestBuilder post, TransactionRequest transactionRequest) {
        List<Callable<TransactionResponse>> depositTasks = new ArrayList<>();

        for (int i = 0; i < concurrentRequests; i++) {
            depositTasks.add(() -> {
                MvcResult result = mvc.perform(post
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionRequest)))
                        .andExpect(status().isOk())
                        .andReturn();

                String content = result.getResponse().getContentAsString();
                return objectMapper.readValue(content, TransactionResponse.class);
            });
        }
        return depositTasks;
    }
}
