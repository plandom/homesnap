package com.poc.homesnap.homesnap.coin;

import com.tei.eziam.iam.domain.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinService {

    private final UserBalanceRepository balanceRepo;
    private final CoinTransactionRepository transactionRepo;

    @Transactional
    public void addCoins(UserId userId, int amount) {
        UserBalance balance = balanceRepo.findById(userId)
            .orElse(new UserBalance(userId, 0));
        balance.addCoins(amount);
        balanceRepo.save(balance);

        CoinTransaction tx = new CoinTransaction(userId, amount, CoinTransaction.TransactionType.CREDIT);
        transactionRepo.save(tx);
        log.info("Added coins for user {} with amount {}", userId, amount);
    }

    @Transactional
    public void deductCoins(UserId userId, int amount) {
        UserBalance balance = balanceRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        balance.deductCoins(amount);
        balanceRepo.save(balance);

        CoinTransaction tx = new CoinTransaction(userId, -amount, CoinTransaction.TransactionType.DEBIT);
        transactionRepo.save(tx);
        log.info("Deducted coins for user {} with amount {}", userId, amount);
    }
}
