package com.mjanputra.tx.tx_engine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mjanputra.tx.tx_engine.service.BalanceService;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    private final BalanceService balanceService;    

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }   

    @GetMapping("/{accountId}")
    public long get(@PathVariable String accountId) {
        return balanceService.getBalance(accountId);
    }

    @PostMapping("/{accountId}/set/{amount}")
    public long set(@PathVariable String accountId, @PathVariable long amount) {
        balanceService.setBalance(accountId, amount);
        return amount;
    }
}
