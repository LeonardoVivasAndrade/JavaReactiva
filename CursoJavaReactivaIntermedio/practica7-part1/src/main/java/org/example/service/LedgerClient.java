package org.example.service;

import org.example.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LedgerClient {
    private static final Logger log = LoggerFactory.getLogger(LedgerClient.class);
    private final WebClient client;

    public LedgerClient(WebClient client) {
        this.client = client;
    }

    public Mono<Transaction> postEntry(Transaction transaction) {
        log.info("POST /ledger/entries - Request body: {}", transaction);

        return client.post()
                .uri("/ledger/entries")
                .bodyValue(transaction)
                .retrieve()
                .bodyToMono(Transaction.class)
                //para hacer log
                .doOnNext(response -> log.info("Response from /ledger/entrie: {}", response))
                .doOnError(e -> log.error("Error posting to /ledger/entrie: {}", e.getMessage()));
    }
}