package com.poc.homesnap.homesnap.coin;

public enum CoinProduct {
    COIN_100("price_1RdUpQHb4TtyICkwiUvJuYJu", 200),
    COIN_500("price_1RdUpQHb4TtyICkwBHeLdAwt", 500),
    COIN_2000("price_1RdUpQHb4TtyICkwmyNeNHdv", 2000);

    private final String id;
    private final int value;

    CoinProduct(String id, int value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return value;
    }
}

