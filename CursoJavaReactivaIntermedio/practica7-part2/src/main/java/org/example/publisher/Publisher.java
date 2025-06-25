package org.example.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TransactionDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import static org.example.config.AMQPConfig.*;

@Slf4j
@Component
public class Publisher {
    private final AmqpTemplate template;

    public Publisher(AmqpTemplate template) {
        this.template = template;
    }

    public void publishCreated(TransactionDto tx){
        template.convertAndSend(EXCHANGE, ROUTING_KEY, tx);
        log.info("Publisher: Evento transacción enviado exitosamente: {}", tx);
    }

    public void publishAudit(TransactionDto tx){
        template.convertAndSend(EXCHANGE, AUDIT_ROUTING_KEY, tx);
        log.info("Publisher: Evento auditoria enviado exitosamente: {}", tx);
    }
}
