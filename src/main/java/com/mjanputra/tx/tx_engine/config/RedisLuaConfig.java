package com.mjanputra.tx.tx_engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

// Creates a bean for executing a Lua script in Redis
// It prepares the script so it can be easily injected and used  into other parts of the application 
// Like a service that handles transactions
@Configuration
public class RedisLuaConfig {
    @Bean
    public DefaultRedisScript<Long> atomicDebitScript(){
        // Load the Lua script from the classpath
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        
        // Set the location of the Lua script
        redisScript.setLocation(new ClassPathResource("lua/atomic_debit.lua"));
        // Set the expected result type
        redisScript.setResultType(Long.class);
        return redisScript;
    }

}
