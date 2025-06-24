package org.example.service;

import org.example.dto.CashRequestDto;
import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.example.model.TransactionStatus;
import org.example.model.TransactionType;
import org.example.repository.TransactionRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final LedgerClient ledger;//libro contable

    public TransactionService(TransactionRepository transactionRepository, LedgerClient ledger) {
        this.transactionRepository = transactionRepository;
        this.ledger = ledger;
    }

    //algo que ingresa a una cuenta
    public Mono<TransactionDto> cashIn(CashRequestDto req){
        Transaction tx = Transaction
                .builder()
                .amount(req.amount())
                .currency(req.currency())
                .type(TransactionType.CASH_IN)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        return transactionRepository
                .save(tx)
                .flatMap(ledger::postEntry)
                .map(this::toDto)
                //resiliencia intenta 3 veces y espera 1 seg
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                // se hace un rollback
                .onErrorResume(e -> rollback(tx,e));
    }

    //algo que ingresa a una cuenta
    public Mono<TransactionDto> cashOut(CashRequestDto req){
        Transaction tx = Transaction
                .builder()
                .amount(req.amount())
                .currency(req.currency())
                .type(TransactionType.CASH_OUT)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        return transactionRepository
                .save(tx)
                .flatMap(ledger::postEntry)
                .map(this::toDto)
                //resiliencia intenta 3 veces y espera 1 seg
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                // se hace un rollback
                .onErrorResume(e -> rollback(tx,e));
    }

    public Mono<TransactionDto> findById(String id) {
        return transactionRepository
                .findById(id)
                .map(this::toDto);
                //se valida empty en el handler
//                .switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()));
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