package com.poc.homesnap.homesnap.coin;

import com.tei.eziam.iam.domain.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "user_balance")
public class UserBalance {

    @EmbeddedId
    private UserId userId;

    @Column(nullable = false)
    private int balance;

    @Version
    private int version;  // Optimistic Locking for concurrency safety

    protected UserBalance() {
        // JPA
    }

    public UserBalance(UserId userId, int initialBalance) {
        this.userId = userId;
        this.balance = initialBalance;
    }

    public UserId getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }

    public void addCoins(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.balance += amount;
    }

    public void deductCoins(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (this.balance < amount) throw new InsufficientBalanceException("Insufficient balance to deduct " + amount + " coins");
        this.balance -= amount;
    }
}

