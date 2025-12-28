package com.mjanputra.tx.tx_engine.service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.mjanputra.tx.tx_engine.dto.TransactionEvent;

@Service
public class TransactionProducer {
    // inject KafkaTemplate to send messages
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private final String topic = "tx.transactions";

    public TransactionProducer(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TransactionEvent event) {
        kafkaTemplate.send(topic, event.accountId(), event);
    }
 }