package com.example.mylittlebank.service;

import com.example.mylittlebank.api.request.PeriodTransactionsRequest;
import com.example.mylittlebank.api.request.TransactionRequest;
import com.example.mylittlebank.api.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse withdrawMoney(TransactionRequest transactionRequest);

    TransactionResponse depositMoney(TransactionRequest transactionRequest);

    TransactionResponse transferMoney(TransactionRequest transactionRequest, Long accountNumberTo);

    List<TransactionResponse> getAllTransactionsForPeriod(PeriodTransactionsRequest periodTransactionsRequest);
}
