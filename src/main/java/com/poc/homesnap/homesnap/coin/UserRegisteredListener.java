package com.poc.homesnap.homesnap.coin;

import com.tei.ezcommon.common.events.UserRegistered;
import com.tei.eziam.iam.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegisteredListener {

    private final UserBalanceRepository userBalanceRepository;

    @EventListener
    public void onUserRegistered(UserRegistered event) {
        log.info("Received UserRegistered event: {}", event);
        UserId userId = new UserId(event.getId());
        userBalanceRepository.save(new UserBalance(userId, 300));
    }
}
