package com.shimnssso.mycointong.setting;

class CoinItem {
    private boolean isSelected = false;
    private int coinId;
    private String coinFullName;

    CoinItem(int coinId, String coinFullName) {
        this.coinId = coinId;
        this.coinFullName = coinFullName;
    }

    boolean isSelected() {
        return isSelected;
    }

    void setSelected(boolean selected) {
        isSelected = selected;
    }

    int getCoinId() {
        return coinId;
    }

    String getCoinFullName() {
        return coinFullName;
    }
}
