package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

//public enum TransactionStatus { PENDING, POSTED, FAILED }
//public enum TransactionType { CASH_IN, CASH_OUT }

@Data
@Getter
@Setter
@Builder
@Document("transactions") //entidad de mongo
public class Transaction {
    @Id
    private String id;

    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currency;

    private TransactionType type;
    private TransactionStatus status;
    private Instant createdAt;

    public Transaction(){}

    public Transaction(String id, BigDecimal amount, String currency,
                       TransactionType type, TransactionStatus status, Instant createdAt) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
    }

//    public static class Builder{
//        private String id;
//        private BigDecimal amount;
//        private String currency;
//        private TransactionType type;
//        private TransactionStatus status;
//        private Instant createdAt;
//
//        public Builder amount(BigDecimal amount){
//            this.amount = amount;
//            return this;
//        }
//
//        public Builder amount(BigDecimal amount){
//            this.amount = amount;
//            return this;
//        }
//    }
}