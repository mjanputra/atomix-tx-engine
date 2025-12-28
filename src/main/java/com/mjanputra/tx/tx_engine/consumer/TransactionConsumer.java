package com.mjanputra.tx.tx_engine.consumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.mjanputra.tx.tx_engine.dto.TransactionEvent;
import com.mjanputra.tx.tx_engine.service.BalanceService;

// this class listens to transactions event from kafka and processes them
// Sits in the background and consumes messages from the "tx.transactions" topic
@Component
public class TransactionConsumer {
    private final BalanceService balanceService;

    public TransactionConsumer(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @KafkaListener(topics = "tx.transactions", groupId = "tx-engine-consumer")
    public void onMessage(TransactionEvent event, Acknowledgment ack) {
        // Apply the transaction to the balance service
        if (event.accountId() == null) {
           throw new IllegalArgumentException("accountId is null in transaction event");
        }
        balanceService.debit(event.accountId(), event.amount());
        ack.acknowledge();
    }

    
}
