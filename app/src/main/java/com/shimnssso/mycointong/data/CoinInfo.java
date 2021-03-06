package com.shimnssso.mycointong.data;

public class CoinInfo {
    public final int coinId;
    public final String coin;
    public final String currency;
    public final String exchange;
    public final String chartCoinone;
    public final double avgPrice;
    public final double quantity;

    public CoinInfo(int coinId, String coin, String currency, String exchange, String chartCoinone, double avgPrice, double quantity) {
        this.coinId = coinId;
        this.coin = coin;
        this.currency = currency;
        this.exchange = exchange;
        this.chartCoinone = chartCoinone;
        this.avgPrice = avgPrice;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CoinInfo [" + coinId + ", " + coin + ", " + currency + ", " + exchange + ", " + chartCoinone + ", avgPrice: " + avgPrice + ", quantity: " + quantity +"]";
    }
}
