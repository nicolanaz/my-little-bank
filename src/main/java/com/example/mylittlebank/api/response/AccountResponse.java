package com.example.mylittlebank.api.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountResponse {

    @NotNull
    private Long accountNumber;

    @NotNull
    private Integer amount;

    @NotNull
    private LocalDate openingDate;

    @NotNull
    private Integer validityPeriod;
}
