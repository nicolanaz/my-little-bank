package com.example.mylittlebank.service;

import com.example.mylittlebank.api.request.PeriodTransactionsRequest;
import com.example.mylittlebank.api.request.TransactionRequest;
import com.example.mylittlebank.api.response.TransactionResponse;
import com.example.mylittlebank.domain.Account;
import com.example.mylittlebank.domain.Transaction;
import com.example.mylittlebank.domain.TransactionType;
import com.example.mylittlebank.exception.AccountNotFoundException;
import com.example.mylittlebank.exception.NotEnoughMoneyException;
import com.example.mylittlebank.mapper.TransactionMapper;
import com.example.mylittlebank.repository.AccountRepository;
import com.example.mylittlebank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionResponse withdrawMoney(TransactionRequest transactionRequest) {
        Long accountNumber = transactionRequest.getAccountNumber();
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber, HttpStatus.NOT_FOUND));

        if (transactionRequest.getAmount() > account.getAmount()) {
            throw new NotEnoughMoneyException("Not enough money on account with number: " + accountNumber, HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = transactionMapper.mapToTransaction(transactionRequest);
        getMoneyFromAccount(account, transaction);

        return transactionMapper.mapToResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse depositMoney(TransactionRequest transactionRequest) {
        Long accountNumber = transactionRequest.getAccountNumber();
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber, HttpStatus.NOT_FOUND));

        Transaction transaction = transactionMapper.mapToTransaction(transactionRequest);
        putMoneyToAccount(account, transaction);

        return transactionMapper.mapToResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse transferMoney(TransactionRequest transactionRequest, Long accountNumberTo) {
        Long accountNumberFrom = transactionRequest.getAccountNumber();
        Account accountFrom = accountRepository.findAccountByAccountNumber(accountNumberFrom)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumberFrom, HttpStatus.NOT_FOUND));
        Account accountTo = accountRepository.findAccountByAccountNumber(accountNumberTo)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumberTo, HttpStatus.NOT_FOUND));

        Transaction transactionFrom = transactionMapper.mapToTransaction(transactionRequest);
        getMoneyFromAccount(accountFrom, transactionFrom);

        Transaction transactionTo = transactionMapper.mapToTransaction(transactionRequest);
        transactionTo.setType(TransactionType.DEPOSIT);
        putMoneyToAccount(accountTo, transactionTo);

        return transactionMapper.mapToResponse(transactionFrom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactionsForPeriod(PeriodTransactionsRequest periodTransactionsRequest) {
        Long userId = periodTransactionsRequest.getUserId();
        LocalDateTime startDate = LocalDateTime.of(periodTransactionsRequest.getStartDate(), LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(periodTransactionsRequest.getEndDate(), LocalTime.MAX);

        return transactionMapper.mapToResponseList(
                transactionRepository.findTransactionsForUserInPeriod(userId, startDate, endDate)
        );
    }

    private void getMoneyFromAccount(Account account, Transaction transaction) {
        if (transaction.getAmount() > account.getAmount()) {
            throw new NotEnoughMoneyException("Not enough money on account with number: " + account.getAccountNumber(), HttpStatus.BAD_REQUEST);
        }
        account.getTransactions().add(transaction);
        account.setAmount(account.getAmount() - transaction.getAmount());
        accountRepository.save(account);
    }

    private void putMoneyToAccount(Account account, Transaction transaction) {
        account.getTransactions().add(transaction);
        if (account.getAmount() == null) {
            account.setAmount(transaction.getAmount());
        } else {
            account.setAmount(account.getAmount() + transaction.getAmount());
        }
        accountRepository.save(account);
    }
}