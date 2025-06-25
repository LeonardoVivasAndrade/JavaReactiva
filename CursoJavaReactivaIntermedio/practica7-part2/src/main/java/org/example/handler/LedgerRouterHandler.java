package org.example.handler;

import org.example.model.TransactionStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class LedgerRouterHandler {


    @Bean(name = "ledgerRouter")
    public RouterFunction<ServerResponse> router(){
        return RouterFunctions.route(
                POST("/ledger/entries"),
                this::createEntry);
    }

    private Mono<ServerResponse> createEntry(ServerRequest request) {
        return request
                //le dice a Spring WebFlux que quieres deserializar el cuerpo
                // como un Map<String, Object>, no como un Map sin tipo.
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(body -> {
                    body.replace("status", TransactionStatus.POSTED);
                    body.replace("createdAt", Instant.now());
                    body.replace("id", UUID.randomUUID().toString());

                    return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                });
    }
}
