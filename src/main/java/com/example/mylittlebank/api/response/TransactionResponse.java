package com.example.mylittlebank.api.response;

import com.example.mylittlebank.domain.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionResponse {

    @NotNull
    private Long id;

    @NotNull
    private Long accountNumber;

    @NotNull
    private TransactionType type;

    @NotNull
    private Integer amount;

    @NotNull
    private LocalDateTime dateTime;
}
