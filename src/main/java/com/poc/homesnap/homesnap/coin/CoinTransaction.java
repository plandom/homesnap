package com.poc.homesnap.homesnap.coin;

import com.tei.eziam.iam.domain.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "coin_transaction")
public class CoinTransaction {

    @EmbeddedId
    private TransactionId id;

    @Embedded
    private UserId userId;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected CoinTransaction() {}

    public CoinTransaction(UserId userId, int amount, TransactionType type) {
        this.id = TransactionId.random();
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.createdAt = Instant.now();
    }

    public enum TransactionType {
        CREDIT, DEBIT
    }
}
