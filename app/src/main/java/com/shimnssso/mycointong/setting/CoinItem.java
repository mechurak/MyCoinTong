package com.shimnssso.mycointong.setting;

class CoinItem {
    private boolean isSelected = false;
    private String coinFullName;

    CoinItem(String coinFullName) {
        this.coinFullName = coinFullName;
    }

    boolean isSelected() {
        return isSelected;
    }

    void setSelected(boolean selected) {
        isSelected = selected;
    }

    String getCoinFullName() {
        return coinFullName;
    }
}
