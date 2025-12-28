package com.mjanputra.tx.tx_engine.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    private final StringRedisTemplate redis;
    
    public BalanceService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private String key(String accountId) {
        return "balance:" + accountId;
    }

    public long getBalance(String accountId) {
        String value = redis.opsForValue().get(key(accountId));
        return (value == null) ? 0L : Long.parseLong(value);
    }

    public long setBalance(String accountId, long amount) {
        redis.opsForValue().set(key(accountId), Long.toString(amount));
        return amount;
    }

    // Not atomic yet, Lua will fix this
    public long debit(String accountId, long amount) {
        long current = getBalance(accountId);
        
        if (current < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        long next = current - amount;
        redis.opsForValue().set(key(accountId), Long.toString(next));
        return next;
    }
}
