package com.example.mylittlebank.service;

import com.example.mylittlebank.api.request.AccountRequest;
import com.example.mylittlebank.api.response.AccountResponse;

public interface AccountService {

    AccountResponse saveAccount(Long userId, AccountRequest accountRequest);

    void deleteAccount(Long accountNumber);
}
