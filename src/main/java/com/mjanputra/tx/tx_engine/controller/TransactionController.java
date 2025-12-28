package com.mjanputra.tx.tx_engine.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mjanputra.tx.tx_engine.dto.TransactionRequest;
import com.mjanputra.tx.tx_engine.service.BalanceService;

import jakarta.validation.Valid;

@RequestMapping("/transaction")
public class TransactionController {
    private final BalanceService balanceService;

    public TransactionController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping
    public long post(@Valid @RequestBody TransactionRequest request) {
        return balanceService.debit(request.fromAccountId(), request.amount());
    }
}
