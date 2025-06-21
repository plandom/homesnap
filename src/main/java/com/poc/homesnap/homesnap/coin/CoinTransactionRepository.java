package com.poc.homesnap.homesnap.coin;

import com.tei.eziam.iam.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, TransactionId> {
    List<CoinTransaction> findByUserId(UserId userId);
}
