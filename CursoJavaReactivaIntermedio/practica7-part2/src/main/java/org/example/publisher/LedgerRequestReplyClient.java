package org.example.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.example.config.AMQPConfig.EXCHANGE_LEDGER;
import static org.example.config.AMQPConfig.ROUTING_LEDGER_KEY;

@Slf4j
@Component
public class LedgerRequestReplyClient {

    private final RabbitTemplate rabbitTemplate;

    public LedgerRequestReplyClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Transaction> sendTransaction(TransactionDto tx) {
        return Mono.fromCallable(() ->
                        (Transaction) rabbitTemplate.convertSendAndReceive(
                                EXCHANGE_LEDGER,
                                ROUTING_LEDGER_KEY,
                                tx
                        )
                )
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(transaction -> {
                    if (transaction != null) {
                        log.info("LedgerRequestReplyClient: Transacción enviada exitosamente: {}", transaction);
                    } else {
                        log.warn("LedgerRequestReplyClient: La transacción fue enviada pero no se recibió respuesta.");
                    }
                });
    }
}
