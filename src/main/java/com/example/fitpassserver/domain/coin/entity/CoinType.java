package com.example.fitpassserver.domain.coin.entity;

public enum CoinType {
    COIN_1("코인 1개", 550, 30),
    COIN_5("코인 5개", 2750, 30),
    COIN_10("코인 10개", 5500, 30),
    COIN_20("코인 20개", 11000, 30),
    COIN_30("코인 30개", 16500, 30),
    COIN_180("코인 180개", 99000, 90),
    COIN_300("코인 300개", 165000, 180);
    private String description;
    private int price;
    private int deadLine;

    CoinType(String description, int price, int deadLine) {
        this.description = description;
        this.price = price;
        this.deadLine = deadLine;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public long getDeadLine() {
        return deadLine;
    }

    public static CoinType getCoinType(int price, int quantity) {
        int value = price / quantity;
        CoinType unit = null;
        for (CoinType coinType : CoinType.values()) {
            if (coinType.price == value) {
                unit = coinType;
                break;
            }
        }
        return unit;
    }
}