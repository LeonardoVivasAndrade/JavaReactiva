package org.example.handler;

import org.example.dto.CashRequestDto;
import org.example.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {
    private final TransactionService transactionService;

    public TransactionHandler(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public Mono<ServerResponse> cashIn(ServerRequest request){
        return request.bodyToMono(CashRequestDto.class)
                .flatMap(transactionService::cashIn)
                .flatMap(transactionDto ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(transactionDto)
                );
    }

    public Mono<ServerResponse> cashOut(ServerRequest request){
        return request.bodyToMono(CashRequestDto.class)
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

    @Bean
    public RouterFunction<ServerResponse> routes(TransactionHandler handler) {
        return RouterFunctions.route()
                .POST("/cash-in", handler::cashIn)
                .POST("/cash-out", handler::cashOut)
                .GET("/transaction/{id}", handler::findBy)
                .build();
    }
}