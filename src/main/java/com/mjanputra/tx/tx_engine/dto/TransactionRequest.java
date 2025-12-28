package com.mjanputra.tx.tx_engine.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
public record TransactionRequest (
    @NotBlank String accountId,
    @NotBlank String toAccountId,
    @Positive long amount
) {}