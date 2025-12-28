package com.mjanputra.tx.tx_engine.dto;

public record TransactionEvent (
    String transactionId,
    String accountId,
    long amount
){}
