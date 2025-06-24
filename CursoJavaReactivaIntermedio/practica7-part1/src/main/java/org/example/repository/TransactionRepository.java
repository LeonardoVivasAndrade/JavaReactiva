package org.example.repository;

import org.example.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, String> {
}
