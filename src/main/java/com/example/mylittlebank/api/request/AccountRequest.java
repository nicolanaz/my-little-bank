package com.example.mylittlebank.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountRequest {

    @NotNull(message = "Validity period cannot be empty")
    private Integer validityPeriod;
}
