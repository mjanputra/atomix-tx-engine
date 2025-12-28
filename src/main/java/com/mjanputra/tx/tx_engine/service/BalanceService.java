package com.mjanputra.tx.tx_engine.service;
import java.util.List;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;


@Service
public class BalanceService {
    private final StringRedisTemplate redis;
    // inject the Lua script bean
    private final DefaultRedisScript<Long> atomicDebitScript;

    
    public BalanceService(StringRedisTemplate redis, DefaultRedisScript<Long> atomicDebitScript) {
        this.redis = redis;
        this.atomicDebitScript = atomicDebitScript;
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

    // use Lua script to perform atomic debit operation
    public long debit(String accountId, long amount) {
        String balanceKey = key(accountId);
        Long result = redis.execute(atomicDebitScript, 
                List.of(balanceKey),
                Long.toString(amount)
        );

        if (result == null) {
            throw new IllegalStateException("Failed to execute debit operation");
        }
        if (result == -1L) {
            throw new IllegalArgumentException("Insufficient balance for account: " + accountId);
        }   
        return result;
    }
}
