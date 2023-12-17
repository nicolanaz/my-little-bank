package com.example.mylittlebank.controller;

import com.example.mylittlebank.api.request.PeriodTransactionsRequest;
import com.example.mylittlebank.api.request.TransactionRequest;
import com.example.mylittlebank.api.response.TransactionResponse;
import com.example.mylittlebank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<List<TransactionResponse>> getTransactionsForPeriod(@RequestBody @Validated PeriodTransactionsRequest periodTransactionsRequest) {
        return ResponseEntity.ok(transactionService.getAllTransactionsForPeriod(periodTransactionsRequest));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> getMoney(@RequestBody @Validated TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.withdrawMoney(transactionRequest));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> putMoney(@RequestBody @Validated TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.depositMoney(transactionRequest));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transferMoney(@RequestBody @Validated TransactionRequest transactionRequest,
                                                             @RequestParam("to") Long accountNumberTo) {
        return ResponseEntity.ok(transactionService.transferMoney(transactionRequest, accountNumberTo));
    }
}
