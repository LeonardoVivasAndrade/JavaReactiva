package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {
    public static final String EXCHANGE = "transaction.exchange";
    public static final String ROUTING_KEY = "transaction.created";
    public static final String QUEUE_KEY = "transaction.created.queue";


    //ledger
    public static final String EXCHANGE_LEDGER = "ledger.exchange";
    public static final String ROUTING_LEDGER_KEY = "ledger.entry.request";
    public static final String QUEUE_LEDGER_KEY = "ledger.entry.request.queue";

    //audit
    public static final String AUDIT_ROUTING_KEY = "transaction.audit";
    public static final String AUDIT_QUEUE_KEY = "audit.queue";

    //estas configuraciones se crean en el broker,
    // esto no siempre se puede hacer depende de los permisos que se tengan, lo ideal es siempre hacerlo
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_KEY, true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange  exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    //ledger
    @Bean
    public DirectExchange ledgerExchange() {
        return new DirectExchange(EXCHANGE_LEDGER);
    }

    @Bean
    public Queue ledgetQueue(){
        return new Queue(QUEUE_LEDGER_KEY, true);
    }

    @Bean
    public Binding ledgerBinding() {
        return BindingBuilder.bind(ledgetQueue()).to(ledgerExchange()).with(ROUTING_LEDGER_KEY);
    }
    //ledger

    //audit
    @Bean
    public Queue auditQueue(){
        return new Queue(AUDIT_QUEUE_KEY, true);
    }

    @Bean
    public Binding auditBinding(Queue auditQueue, DirectExchange  exchange){
        return BindingBuilder.bind(auditQueue).to(exchange).with(AUDIT_ROUTING_KEY);
    }
    //audit

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory, MessageConverter messageConverter){
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(messageConverter);
        template.setReplyTimeout(5000);
        template.setUseTemporaryReplyQueues(true);//hace reintentos
        return template;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory factory){
        return new RabbitAdmin(factory);
    }


}
