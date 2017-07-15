package com.shimnssso.mycointong;

public class Constant {
    public static class CoinName {
        public static String BTC_KORBIT = "BTC_KRW(Korbit)";
        public static String BTC_BITHUM = "BTC_KRW(Bithum)";
        public static String BTC_COINONE = "BTC_KRW(Coinone)";
        public static String ETH_KORBIT = "ETH_KRW(Korbit)";
        public static String ETH_BITHUM = "ETH_KRW(Bithum)";
        public static String ETH_COINONE = "ETH_KRW(Coinone)";
        public static String ETC_KORBIT = "ETC_KRW(Korbit)";
        public static String ETC_BITHUM = "ETC_KRW(Bithum)";
        public static String ETC_COINONE = "ETC_KRW(Coinone)";
        public static String DASH_BITHUM = "DASH_KRW(Bithum)";
        public static String LTC_BITHUM = "LTC_KRW(Bithum)";
        public static String XRP_BITHUM = "XRP_KRW(Bithum)";
        public static String XRP_COINONE = "XRP_KRW(Coinone)";
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
        public static final String CoinName = "coin_name";
        public static final String AvgPrice = "avg_price";
        public static final String Quantity = "quantity";
    }
}