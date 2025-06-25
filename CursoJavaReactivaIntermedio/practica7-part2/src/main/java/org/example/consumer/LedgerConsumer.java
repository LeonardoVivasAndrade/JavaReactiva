package org.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Transaction;
import org.example.model.TransactionStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.example.config.AMQPConfig.QUEUE_LEDGER_KEY;

@Slf4j
@Component
public class LedgerConsumer {

    @RabbitListener(queues = QUEUE_LEDGER_KEY)
    public Transaction receive(Transaction tx) {
        tx.setStatus(TransactionStatus.POSTED);
        tx.setCreatedAt(Instant.now());
        log.info("LedgerRequestReplyClient: Transacción recibida exitosamente: {}", tx);
        return tx;
    }
}
