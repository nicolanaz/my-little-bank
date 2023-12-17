package com.example.mylittlebank.mapper;

import com.example.mylittlebank.api.request.AccountRequest;
import com.example.mylittlebank.api.response.AccountResponse;
import com.example.mylittlebank.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountMapper {
    @Mapping(target = "openingDate", expression = "java(java.time.LocalDate.now())")
    Account mapToAccount(AccountRequest accountRequest);

    AccountResponse mapToResponse(Account account);
}
