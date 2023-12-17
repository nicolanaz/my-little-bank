package com.example.mylittlebank.api.request;

import com.example.mylittlebank.domain.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionRequest {

    @NotNull(message = "Account number cannot be empty")
    private Long accountNumber;

    @NotNull(message = "Transaction type cannot be empty")
    private TransactionType type;

    @NotNull(message = "Transaction amount cannot be empty")
    private Integer amount;
}
