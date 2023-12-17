package com.example.mylittlebank.service;

import com.example.mylittlebank.api.request.AccountRequest;
import com.example.mylittlebank.api.response.AccountResponse;
import com.example.mylittlebank.domain.Account;
import com.example.mylittlebank.domain.User;
import com.example.mylittlebank.exception.AccountNotFoundException;
import com.example.mylittlebank.exception.UserNotFoundException;
import com.example.mylittlebank.mapper.AccountMapper;
import com.example.mylittlebank.repository.AccountRepository;
import com.example.mylittlebank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponse saveAccount(Long userId, AccountRequest accountRequest) {
        Account account = accountMapper.mapToAccount(accountRequest);

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND));
        user.getAccounts().add(account);
        userRepository.save(user);

        return accountMapper.mapToResponse(account);
    }

    @Override
    @Transactional
    public void deleteAccount(Long accountNumber) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                        .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber, HttpStatus.NOT_FOUND));
        accountRepository.delete(account);
    }
}
