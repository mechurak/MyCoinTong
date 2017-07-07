package com.shimnssso.mycointong.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shimnssso.mycointong.Constant;

import java.util.ArrayList;
import java.util.Arrays;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";
    private static DbHelper mInstance;

    private DbHelper(Context context) {
        super(context, DbMeta.DATABASE_NAME, null, DbMeta.DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context) {
        // for debug
        //context.deleteDatabase(DbMeta.DATABASE_NAME);

        if (mInstance == null) {
            mInstance = new DbHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate()");
        db.execSQL("CREATE TABLE " + DbMeta.GlobalTableMeta.TABLE_NAME + " ("
                + DbMeta.GlobalTableMeta.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DbMeta.GlobalTableMeta.UPDATE_TIME + " INTEGER"
                + " )"
        );
        db.execSQL("INSERT INTO " + DbMeta.GlobalTableMeta.TABLE_NAME + " VALUES (null,0)");

        db.execSQL("CREATE TABLE " + DbMeta.CoinTableMeta.TABLE_NAME + " ("
                + DbMeta.CoinTableMeta.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DbMeta.CoinTableMeta.COIN + " TEXT NOT NULL, "
                + DbMeta.CoinTableMeta.INTEREST + " INTEGER DEFAULT 1, "
                + DbMeta.CoinTableMeta.LIST_ORDER + " INTEGER, "
                + DbMeta.CoinTableMeta.MY_PRICE + " REAL, "
                + DbMeta.CoinTableMeta.MY_VOLUME + " REAL, "
                + DbMeta.CoinTableMeta.CHART_COINONE + " TEXT, "
                + DbMeta.CoinTableMeta.UNIT_TIME + " TEXT DEFAULT '" + Constant.UnitTime.FIFTEEN_MIN + "'"
                + " )"
        );

        String[][] coinList = {
                {Constant.CoinName.BTC_KORBIT, "0", Constant.ChartSite.BTC_KORBIT},
                {Constant.CoinName.BTC_BITHUM, "1", Constant.ChartSite.BTC_BITHUM},
                {Constant.CoinName.BTC_COINONE, "2", Constant.ChartSite.BTC_COINONE},
                {Constant.CoinName.ETH_KORBIT, "3", Constant.ChartSite.ETH_KORBIT},
                {Constant.CoinName.ETH_BITHUM, "4", Constant.ChartSite.ETH_BITHUM},
                {Constant.CoinName.ETH_COINONE, "5", Constant.ChartSite.ETH_COINONE},
                {Constant.CoinName.ETC_KORBIT, "6", Constant.ChartSite.ETC_KORBIT},
                {Constant.CoinName.ETC_BITHUM, "7", Constant.ChartSite.ETC_BITHUM},
                {Constant.CoinName.ETC_COINONE, "8", Constant.ChartSite.ETC_COINONE},
                {Constant.CoinName.DASH_BITHUM, "9", Constant.ChartSite.DASH_BITHUM},
                {Constant.CoinName.LTC_BITHUM, "10", Constant.ChartSite.LTC_BITHUM},
                {Constant.CoinName.XRP_BITHUM, "11", Constant.ChartSite.XRP_BITHUM},
                {Constant.CoinName.XRP_COINONE, "12", Constant.ChartSite.XRP_COINONE},
        };

        String sql = "INSERT INTO " + DbMeta.CoinTableMeta.TABLE_NAME + " VALUES (null,?,null,?,null,null,?,null)";
        SQLiteStatement statement = db.compileStatement(sql);
        for (String[] row : coinList) {
            Log.d(TAG, "inserting " + Arrays.toString(row));

            statement.clearBindings();
            statement.bindAllArgsAsStrings(row);
            statement.executeInsert();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade(). old: " + oldVersion + ", new: " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DbMeta.GlobalTableMeta.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbMeta.CoinTableMeta.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<String[]> getInterestingCoinList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                DbMeta.CoinTableMeta.COIN,
                DbMeta.CoinTableMeta.CHART_COINONE
        };
        ArrayList<String[]> ret = new ArrayList<>();
        Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columns, null, null, null, null, DbMeta.CoinTableMeta.LIST_ORDER);
        if (c != null && c.moveToFirst()) {
            do {
                ret.add(new String[]{c.getString(0), c.getString(1)});
            } while (c.moveToNext());
            c.close();
        }
        Log.d(TAG, "getInterestingCoinList. size:" + ret.size());
        return ret;
    }
}
