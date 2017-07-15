package com.shimnssso.mycointong.data;

public class CoinInfo {
    public final String coinName;
    public final String chartCoinone;
    public final double avgPrice;
    public final double quantity;

    public CoinInfo(String coinName, String chartCoinone, double avgPrice, double quantity) {
        this.coinName = coinName;
        this.chartCoinone = chartCoinone;
        this.avgPrice = avgPrice;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CoinInfo [" + coinName + ", " + chartCoinone + ", avgPrice: " + avgPrice + ", quantity: " + quantity +"]";
    }
}
