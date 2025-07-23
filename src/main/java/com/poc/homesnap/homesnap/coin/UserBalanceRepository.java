package com.poc.homesnap.homesnap.coin;

import com.tei.eziam.iam.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, UserId> {
    UserBalance findUserBalanceByUserId(UserId userId);
}
