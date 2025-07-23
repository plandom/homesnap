package com.poc.homesnap.homesnap.coin;

import com.poc.homesnap.homesnap.design.events.DesignGeneratedEvent;
import com.tei.eziam.iam.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DesignGeneratedListener {

    private final CoinService coinService;

    @EventListener
    public void onDesignGenerated(DesignGeneratedEvent event) {
        log.info("Design Generated event is received {}", event);
        UserId userId = event.getUserId();
        coinService.deductCoins(userId, 30);
    }
}
