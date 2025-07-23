package com.poc.homesnap.homesnap.coin;

import com.tei.eziam.iam.domain.UserId;
import com.tei.ezpayment.payment.application.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class CoinController {

    private final PaymentService paymentService;
    private final CoinService coinService;

    @SneakyThrows
    @PostMapping("/payments/coin-top-up")
    public ResponseEntity<Map<String, String>> coinTopUp(@RequestBody Map<String, String> request) {
        String priceId = request.get("priceId");
        String userId = request.get("userId");

        String checkoutSession = paymentService.getCheckoutSessionUrl(priceId, userId);

        return ResponseEntity.ok(Map.of("url", checkoutSession));
    }

    @GetMapping("/coins/{id}")
    public ResponseEntity<Map<String, Object>> getUserCoins(@PathVariable String id) {
        UserId userId = UserId.fromString(id);
        int coins = coinService.getCoins(userId);
        return ResponseEntity.ok(Map.of("userId", userId, "coins", coins));
    }

}
