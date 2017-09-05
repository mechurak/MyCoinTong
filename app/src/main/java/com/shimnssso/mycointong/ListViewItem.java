package com.shimnssso.mycointong;

public class ListViewItem {
    private String coin;
    private String currency;
    private String exchange;

    // TODO: Consider BigDecimal
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double curPrice;
    private double volume;
    private double myAvgPrice;
    private double myQuantity;
    private String coinoneChartSite;
    private String unitTime = Const.UnitTime.FIFTEEN_MIN;

    ListViewItem(String coin, String currency, String exchange, String coinoneChartSite) {
        this.coin = coin;
        this.currency = currency;
        this.exchange = exchange;
        this.coinoneChartSite = coinoneChartSite;
    }

    public String getCoin() {
        return coin;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExchange() {
        return exchange;
    }

    public String getName() {
        return coin + "/" + currency;
    }

    String getFullName() {
        return coin + "/" + currency + "(" + exchange + ")";
    }

    double getCurPrice() {
        return curPrice;
    }

    double getOpenPrice() {
        return openPrice;
    }

    double getLowPrice() {
        return lowPrice;
    }

    double getHighPrice() {
        return highPrice;
    }

    String getCoinoneChartSite() {
        return coinoneChartSite;
    }

    String getUnitTime() {
        return unitTime;
    }

    public void setPrice(double openPrice, double highPrice, double lowPrice, double curPrice, double volume) {
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.curPrice = curPrice;
        this.volume = volume;
    }

    double getVolume() {
        return volume;
    }

    double getMyAvgPrice() {
        return myAvgPrice;
    }

    void setMyAvgPrice(double myAvgPrice) {
        this.myAvgPrice = myAvgPrice;
    }

    double getMyQuantity() {
        return myQuantity;
    }

    void setMyQuantity(double myQuantity) {
        this.myQuantity = myQuantity;
    }

    public void setUnitTime(String unitTime) {
        this.unitTime = unitTime;
    }

    @Override
    public String toString() {
        return getFullName() + " O: " + openPrice+ ", H: " + highPrice + ", L: " + lowPrice + ", C: " + curPrice + ", V: " + volume;
    }
}