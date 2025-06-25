package org.example.handler;

import org.example.dto.TransactionDto;
import org.example.publisher.LedgerRequestReplyClient;
import org.example.service.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {
    private final TransactionService transactionService;
    private final LedgerRequestReplyClient ledgerClient;

    public TransactionHandler(TransactionService transactionService, LedgerRequestReplyClient ledgerClient) {
        this.transactionService = transactionService;
        this.ledgerClient = ledgerClient;
    }

    public Mono<ServerResponse> cashIn(ServerRequest request){
        return request.bodyToMono(TransactionDto.class)
                .flatMap(ledgerClient::sendTransaction) // Envia por AMQP y espera respuesta
                .flatMap(transactionService::cashIn)
                .flatMap(transactionDto ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(transactionDto)
                );
    }

    public Mono<ServerResponse> cashOut(ServerRequest request){
        return request.bodyToMono(TransactionDto.class)
                .flatMap(ledgerClient::sendTransaction) // Envia por AMQP y espera respuesta
                .flatMap(transactionService::cashOut)
                .flatMap(transactionDto ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(transactionDto)
                );
    }

    public Mono<ServerResponse> findBy(ServerRequest request){
        return transactionService
                .findById(request.pathVariable("id"))
                .flatMap(transactionDto ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(transactionDto)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}