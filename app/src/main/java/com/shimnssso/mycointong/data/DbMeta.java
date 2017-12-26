package com.shimnssso.mycointong.data;

public class DbMeta {
    public static final String DATABASE_NAME = "coin.db";

    /**
     * [1] initial db
     * [2] 2017-11-26: Remove OKCOIN exchange
     *     2017-12-02: Add coins (Bithumb: BTG, Coinone: IOTA, LTC)
     * [3] 2017-12-04: Add coins (Bitfinex: BTG, IOTA)
     * [4] 2017-12-18: Add coin (Bithumb: EOS)
     * [5] 2017-12-19: Add exchange rate to global
     * [6] 2017-12-27: Change link (bitflyer: BTC)
     */
    public static final int DATABASE_VERSION = 6;

    public static final class GlobalTableMeta {
        public static final String TABLE_NAME = "global";

        public static final String ID = "_id";
        public static final String UPDATE_TIME = "update_time";

        public static final String USD_KRW = "usd_krw";  // float
        public static final String USD_JPY = "usd_jpy";  // float
        public static final String UPDATE_TIME_EXCHANGE_RATE = "update_time_exchange_rate"; // long
    }

    public static final class CoinTableMeta {
        public static final String TABLE_NAME = "coin";

        public static final String ID = "_id";
        public static final String COIN = "coin";  // String
        public static final String CURRENCY = "currency";  // String
        public static final String EXCHANGE = "exchange";  // String
        public static final String DEFAULT_LINK = "default_link";  // String
    }

    public static final class InterestTableMeta {
        public static final String TABLE_NAME = "interest";

        public static final String ID = "_id";
        public static final String GROUP_ID = "group_id";  // int
        public static final String COIN_ID = "coin_id";  // int
        public static final String ORDER_IN_GROUP = "order_in_group";  // int
        public static final String MY_PRICE = "my_price";  // float
        public static final String MY_VOLUME = "my_volume";  // float
        public static final String LINK = "link";  // String
    }
}
