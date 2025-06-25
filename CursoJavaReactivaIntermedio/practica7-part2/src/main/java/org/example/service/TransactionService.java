package org.example.service;

import org.example.dto.TransactionDto;
import org.example.publisher.Publisher;
import org.example.model.Transaction;
import org.example.model.TransactionStatus;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.example.model.TransactionType.CASH_IN;
import static org.example.model.TransactionType.CASH_OUT;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final Publisher publisher;

    public TransactionService(TransactionRepository transactionRepository, Publisher publisher) {
        this.transactionRepository = transactionRepository;
        this.publisher = publisher;
    }

    public Mono<TransactionDto> cashIn(Transaction tx){
        tx.setType(CASH_IN);

        return transactionRepository
                .save(tx)
                .map(this::toDto)
                //resiliencia intenta 3 veces y espera 1 seg
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                //publica la transaccion en amqp
                .doOnSuccess(publisher::publishCreated)
                .doOnSuccess(publisher::publishAudit)
                // se hace un rollback
                .onErrorResume(e -> rollback(tx,e));
    }

    public Mono<TransactionDto> cashOut(Transaction tx){
        tx.setType(CASH_OUT);

        return transactionRepository
                .save(tx)
                .map(this::toDto)
                //resiliencia intenta 3 veces y espera 1 seg
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                //publica la transaccion en amqp
                .doOnSuccess(publisher::publishCreated)
                .doOnSuccess(publisher::publishAudit)
                // se hace un rollback
                .onErrorResume(e -> rollback(tx,e));
    }

    public Mono<TransactionDto> findById(String id) {
        return transactionRepository
                .findById(id)
                .map(this::toDto)
                .doOnSuccess(publisher::publishAudit);
    }

    private Mono<TransactionDto> rollback(Transaction tx, Throwable e) {
        tx.setStatus(TransactionStatus.FAILED);
        return transactionRepository
                .save(tx)
                .then(Mono.error(e));
    }

    private TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }

}