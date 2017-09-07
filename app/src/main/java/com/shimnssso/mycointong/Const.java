package com.shimnssso.mycointong;

import android.support.annotation.ColorInt;

public class Const {
    public static class Coin {
        public static final String BTC = "BTC";
        public static final String BCH = "BCH";
        public static final String ETH = "ETH";
        public static final String ETC = "ETC";
        public static final String DASH = "DASH";
        public static final String LTC = "LTC";
        public static final String XRP = "XRP";
    }

    public static class Currency {
        public static final String KRW = "KRW";
        public static final String USD = "USD";
        public static final String BTC = "BTC";
        public static final String CNY = "CNY";
        public static final String JPY = "JPY";
    }

    public static class Exchange {
        public static final String KORBIT = "Korbit";
        public static final String BITHUMB = "Bithumb";
        public static final String COINONE = "Coinone";
        public static final String POLONIEX = "Poloniex";
        public static final String BITFINEX = "Bitfinex";
        public static final String OKCOIN = "OKCoin";
        public static final String BITFLYER = "bitFlyer";

        public static final String CRYPTOWATCH = "Cryptowat.ch";
    }

    public static class UnitTime {
        public static final String ONE_MIN = "1M";
        public static final String THREE_MIN = "3M";
        public static final String FIVE_MIN = "5M";
        public static final String FIFTEEN_MIN = "15M";
        public static final String THIRTY_MIN = "50M";
        public static final String ONE_HOUR = "1H";
        public static final String TWO_HOUR = "2H";
        public static final String FOUR_HOUR = "4H";
        public static final String SIX_HOUR = "6H";
        public static final String ONE_DAY = "1D";
        public static final String ONE_WEEK = "1W";
    }

    public static class RequestCode {
        public static final int HoldingActivity = 1000;
        public static final int SettingActivity = 1001;
        public static final int AddActivity = 1002;
    }

    public static class HoldingIntentKey {
        public static final String Coin = "coin";
        public static final String Currency = "currency";
        public static final String Exchange = "exchange";
        public static final String AvgPrice = "avg_price";
        public static final String Quantity = "quantity";
    }

    public static class Color {
        @ColorInt public static final int LTRED = 0xFFFF7777;
        @ColorInt public static final int LTBLUE = 0xFF5555FF;
    }
}