package com.poc.homesnap.homesnap.coin;

import com.tei.ezcommon.common.events.EventListener;
import com.tei.ezcommon.common.events.PurchaseConfirmed;
import com.tei.eziam.iam.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class CoinPurchaseListener implements EventListener<PurchaseConfirmed> {

    private final CoinService coinService;

    @org.springframework.context.event.EventListener
    @Override
    public void handle(PurchaseConfirmed event) {
        log.info("Received event: {}", event);
        UUID uuid = event.userId();
        String priceId = event.priceId();

        Integer amount = Arrays.stream(CoinProduct.values()).filter(cp -> cp.getId().equals(priceId))
                .findFirst()
                .map(CoinProduct::getValue)
                .orElse(null);

        if (amount == null) {
            log.error("Could not find price for price id {}", priceId);
            throw new IllegalStateException("Could not find price for price id " + priceId);
        }

        coinService.addCoins(new UserId(uuid), amount);
    }
}
