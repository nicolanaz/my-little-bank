package com.example.mylittlebank.mapper;

import com.example.mylittlebank.api.request.UserRequest;
import com.example.mylittlebank.api.response.AccountResponse;
import com.example.mylittlebank.api.response.UpdateUserResponse;
import com.example.mylittlebank.api.response.UserResponse;
import com.example.mylittlebank.domain.Account;
import com.example.mylittlebank.domain.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface UserMapper {
    User mapToUser(UserRequest userRequest);

    UserResponse mapToResponse(User user);

    @IterableMapping(elementTargetType = AccountResponse.class)
    Set<AccountResponse> mapAccountsToResponses(Set<Account> accounts);

    UpdateUserResponse mapToUpdateResponse(User user);
}
