package com.shimnssso.mycointong;

public class ListViewItem {
    private String name;
    private int openPrice;
    private int highPrice;
    private int lowPrice;
    private int curPrice;
    private double volume;
    private int myPrice;

    ListViewItem(String name) {
        this.name = name;
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

    public int getMyPrice() {
        return myPrice;
    }

    public void setMyPrice(int myPrice) {
        this.myPrice = myPrice;
    }

    @Override
    public String toString() {
        return "O: " + openPrice+ ", H: " + highPrice + ", L: " + lowPrice + ", C: " + curPrice + ", V: " + volume;
    }
}