package com.mjanputra.tx.tx_engine.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjanputra.tx.tx_engine.dto.TransactionEvent;
import com.mjanputra.tx.tx_engine.service.TransactionProducer;
// Front door of the application to accept HTTP requests related to transactions
// It receives transaction requests and delegates the processing to the transaction producer
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionProducer producer;

    public TransactionController(TransactionProducer producer) {
        this.producer = producer;
    }

    public record TransactionRequest (
        @JsonProperty("accountId") String accountId, 
        @JsonProperty("amount") Long amount
    ){}

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionRequest req) {
        if (req.accountId() == null || req.amount() == null) {
            return ResponseEntity.badRequest().body("accountId and amount are required");
        }

        String txId = UUID.randomUUID().toString();
        producer.publish(new TransactionEvent(txId, req.accountId(), req.amount()));
        // In Phase 4 you usually return "accepted" since processing is async
        return ResponseEntity.accepted().body(txId);
    }
}
