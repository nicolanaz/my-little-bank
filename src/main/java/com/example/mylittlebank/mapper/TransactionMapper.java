package com.example.mylittlebank.mapper;

import com.example.mylittlebank.api.request.TransactionRequest;
import com.example.mylittlebank.api.response.TransactionResponse;
import com.example.mylittlebank.domain.Transaction;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TransactionMapper {

    @Mapping(target = "dateTime", expression = "java(java.time.LocalDateTime.now())")
    Transaction mapToTransaction(TransactionRequest transactionRequest);

    TransactionResponse mapToResponse(Transaction transaction);

    @IterableMapping(elementTargetType = TransactionResponse.class)
    List<TransactionResponse> mapToResponseList(List<Transaction> transactions);
}
