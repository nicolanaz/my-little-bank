package com.example.mylittlebank.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PeriodTransactionsRequest {

    @NotNull(message = "User id cannot be empty")
    private Long userId;

    @NotNull(message = "Start date cannot be empty")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be empty")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;
}
