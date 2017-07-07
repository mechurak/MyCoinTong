package com.shimnssso.mycointong.data;

import java.util.HashMap;

public class DbMeta {
    public static final String DATABASE_NAME = "coin.db";
    public static final int DATABASE_VERSION = 1;

    public static final class GlobalTableMeta {
        public static final String TABLE_NAME = "global";

        public static final String ID = "_id";
        public static final String UPDATE_TIME = "update_time";
    }

    public static final class CoinTableMeta {
        public static final String TABLE_NAME = "coin";

        public static final String ID = "_id";
        public static final String COIN = "coin";
        public static final String INTEREST = "interest";
        public static final String LIST_ORDER = "list_order";
        public static final String MY_PRICE = "my_price";
        public static final String MY_VOLUME = "my_volume";
        public static final String CHART_COINONE = "chart_coinone";
        public static final String UNIT_TIME = "unit_time";
    }
}
