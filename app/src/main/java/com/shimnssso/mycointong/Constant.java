package com.shimnssso.mycointong;

public class Constant {
    public static class Coin {
        public static String BTC = "BTC";
        public static String ETH = "ETH";
        public static String ETC = "ETC";
        public static String DASH = "DASH";
        public static String LTC = "LTC";
        public static String XRP = "XRP";
    }

    public static class Currency {
        public static String KRW = "KRW";
        public static String USD = "USD";
        public static String BTC = "BTC";
    }

    public static class Exchange {
        public static String KORBIT = "Korbit";
        public static String BITHUMB = "Bithumb";
        public static String COINONE = "Coinone";
        public static String POLONIEX = "Poloniex";
    }

    public static class ChartSite {
        public static String NOT_SUPPORT = "NOT_SUPPORT";

        public static String BTC_KORBIT = "Korbit";
        public static String BTC_BITHUM = "Bithumb";
        public static String BTC_COINONE = "Coinone";
        public static String ETH_KORBIT = NOT_SUPPORT;
        public static String ETH_BITHUM = NOT_SUPPORT;
        public static String ETH_COINONE = "CoinoneETH";
        public static String ETC_KORBIT = NOT_SUPPORT;
        public static String ETC_BITHUM = NOT_SUPPORT;
        public static String ETC_COINONE = "CoinoneETC";
        public static String DASH_BITHUM = NOT_SUPPORT;
        public static String LTC_BITHUM = NOT_SUPPORT;
        public static String XRP_BITHUM = NOT_SUPPORT;
        public static String XRP_COINONE = "CoinoneXRP";
    }

    public static class UnitTime {
        public static String ONE_MIN = "1M";
        public static String THREE_MIN = "3M";
        public static String FIVE_MIN = "5M";
        public static String FIFTEEN_MIN = "15M";
        public static String THIRTY_MIN = "50M";
        public static String ONE_HOUR = "1H";
        public static String TWO_HOUR = "2H";
        public static String FOUR_HOUR = "4H";
        public static String SIX_HOUR = "6H";
        public static String ONE_DAY = "1D";
        public static String ONE_WEEK = "1W";
    }

    public static class RequestCode {
        public static final int HoldingActivity = 1000;
    }

    public static class HoldingIntentKey {
        public static final String Coin = "coin";
        public static final String Currency = "currency";
        public static final String Exchange = "exchange";
        public static final String AvgPrice = "avg_price";
        public static final String Quantity = "quantity";
    }
}