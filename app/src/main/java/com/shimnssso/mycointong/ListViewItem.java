package com.shimnssso.mycointong;

public class ListViewItem {
    private String name;
    private int openPrice;
    private int highPrice;
    private int lowPrice;
    private int curPrice;
    private double volume;
    private double myAvgPrice;
    private double myQuantity;
    private String coinoneChartSite = Constant.ChartSite.NOT_SUPPORT;
    private String unitTime = Constant.UnitTime.FIFTEEN_MIN;

    ListViewItem(String name) {
        this.name = name;
    }

    ListViewItem(String name, String coinoneChartSite) {
        this.name = name;
        this.coinoneChartSite = coinoneChartSite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurPrice() {
        return curPrice;
    }

    public int getOpenPrice() {
        return openPrice;
    }

    public int getLowPrice() {
        return lowPrice;
    }

    public int getHighPrice() {
        return highPrice;
    }

    public String getCoinoneChartSite() {
        return coinoneChartSite;
    }

    public String getUnitTime() {
        return unitTime;
    }

    public void setPrice(int openPrice, int highPrice, int lowPrice, int curPrice, double volume) {
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.curPrice = curPrice;
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    public double getMyAvgPrice() {
        return myAvgPrice;
    }

    public void setMyAvgPrice(double myAvgPrice) {
        this.myAvgPrice = myAvgPrice;
    }

    public double getMyQuantity() {
        return myQuantity;
    }

    public void setMyQuantity(double myQuantity) {
        this.myQuantity = myQuantity;
    }

    public void setCoinoneChartSite(String chartSite) {
        this.coinoneChartSite = chartSite;
    }

    public void setUnitTime(String unitTime) {
        this.unitTime = unitTime;
    }

    @Override
    public String toString() {
        return "O: " + openPrice+ ", H: " + highPrice + ", L: " + lowPrice + ", C: " + curPrice + ", V: " + volume;
    }
}