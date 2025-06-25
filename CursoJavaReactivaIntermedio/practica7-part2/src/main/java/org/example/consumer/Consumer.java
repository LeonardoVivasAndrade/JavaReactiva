package org.example.consumer;

import org.example.dto.TransactionDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.example.config.AMQPConfig.QUEUE_KEY;

@Component
public class Consumer {

    @RabbitListener(queues = QUEUE_KEY)
    public void handle(TransactionDto tx){
//        System.out.println("Evento recibido por el consumer: " + tx);
    }
}
