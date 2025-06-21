package com.poc.homesnap.homesnap.coin;

import com.tei.eziam.iam.domain.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class TransactionId {

    @Column(name = "transaction_id")
    public UUID value;

    public TransactionId(UUID value) {
        this.value = value;
    }

    public TransactionId() {
    }

    public static TransactionId fromString(String value) {
        return new TransactionId(UUID.fromString(value));
    }

    public static TransactionId random() {
        return new TransactionId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that instanceof UserId
            && Objects.equals(this.value, ((UserId) that).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
