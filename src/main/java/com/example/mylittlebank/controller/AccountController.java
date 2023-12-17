package com.example.mylittlebank.controller;

import com.example.mylittlebank.api.request.AccountRequest;
import com.example.mylittlebank.api.response.AccountResponse;
import com.example.mylittlebank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{userId}")
    public ResponseEntity<AccountResponse> create(@PathVariable("userId") Long userId,
                                                  @RequestBody @Validated AccountRequest accountRequest) {
        return ResponseEntity.ok(accountService.saveAccount(userId, accountRequest));
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> delete(@PathVariable("accountNumber") Long accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok().build();
    }
}
