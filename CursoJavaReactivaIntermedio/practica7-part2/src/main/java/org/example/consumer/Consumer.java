package org.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TransactionDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.example.config.AMQPConfig.*;

@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = QUEUE_KEY)
    public void handle(TransactionDto tx){
        log.info("Publisher: Evento transacción recibido exitosamente: {}", tx);
    }

    @RabbitListener(queues = AUDIT_QUEUE_KEY)
    public void handleAudit(TransactionDto tx){
        log.info("Publisher: Evento auditoria recibido exitosamente: {}", tx);
    }
}
