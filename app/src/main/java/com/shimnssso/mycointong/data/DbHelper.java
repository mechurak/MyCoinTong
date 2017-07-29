package com.shimnssso.mycointong.data;

import android.content.ContentValues;
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
                + DbMeta.CoinTableMeta.CURRENCY + " TEXT NOT NULL, "
                + DbMeta.CoinTableMeta.EXCHANGE + " TEXT NOT NULL, "
                + DbMeta.CoinTableMeta.INTEREST + " INTEGER DEFAULT 1, "
                + DbMeta.CoinTableMeta.LIST_ORDER + " INTEGER, "
                + DbMeta.CoinTableMeta.MY_PRICE + " REAL, "
                + DbMeta.CoinTableMeta.MY_VOLUME + " REAL, "
                + DbMeta.CoinTableMeta.CHART_COINONE + " TEXT, "
                + DbMeta.CoinTableMeta.UNIT_TIME + " TEXT DEFAULT '" + Constant.UnitTime.FIFTEEN_MIN + "'"
                + " )"
        );

        String[][] coinList = {
                {Constant.Coin.BTC, Constant.Currency.KRW, Constant.Exchange.KORBIT, "1", "0", Constant.ChartSite.BTC_KORBIT},
                {Constant.Coin.BTC, Constant.Currency.KRW, Constant.Exchange.BITHUMB, "1", "1", Constant.ChartSite.BTC_BITHUM},
                {Constant.Coin.BTC, Constant.Currency.KRW, Constant.Exchange.COINONE, "1", "2", Constant.ChartSite.BTC_COINONE},

                {Constant.Coin.ETH, Constant.Currency.KRW, Constant.Exchange.KORBIT, "1", "3", Constant.ChartSite.ETH_KORBIT},
                {Constant.Coin.ETH, Constant.Currency.KRW, Constant.Exchange.BITHUMB, "1", "4", Constant.ChartSite.ETH_BITHUM},
                {Constant.Coin.ETH, Constant.Currency.KRW, Constant.Exchange.COINONE, "1", "5", Constant.ChartSite.ETH_COINONE},

                {Constant.Coin.ETC, Constant.Currency.KRW, Constant.Exchange.KORBIT, "1", "6", Constant.ChartSite.ETC_KORBIT},
                {Constant.Coin.ETC, Constant.Currency.KRW, Constant.Exchange.BITHUMB, "1", "7", Constant.ChartSite.ETC_BITHUM},
                {Constant.Coin.ETC, Constant.Currency.KRW, Constant.Exchange.COINONE, "1", "8", Constant.ChartSite.ETC_COINONE},

                {Constant.Coin.DASH, Constant.Currency.KRW, Constant.Exchange.BITHUMB, "1", "9", Constant.ChartSite.DASH_BITHUM},
                {Constant.Coin.LTC, Constant.Currency.KRW, Constant.Exchange.BITHUMB, "1", "10", Constant.ChartSite.LTC_BITHUM},

                {Constant.Coin.XRP, Constant.Currency.KRW, Constant.Exchange.BITHUMB, "1", "11", Constant.ChartSite.XRP_BITHUM},
                {Constant.Coin.XRP, Constant.Currency.KRW, Constant.Exchange.COINONE, "1", "12", Constant.ChartSite.XRP_COINONE},
        };

        String sql = "INSERT INTO " + DbMeta.CoinTableMeta.TABLE_NAME + " VALUES (null,?,?,?,?,?,null,null,?,null)";
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

    public ArrayList<CoinInfo> getInterestingCoinList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                DbMeta.CoinTableMeta.COIN,
                DbMeta.CoinTableMeta.CURRENCY,
                DbMeta.CoinTableMeta.EXCHANGE,
                DbMeta.CoinTableMeta.CHART_COINONE,
                DbMeta.CoinTableMeta.MY_PRICE,
                DbMeta.CoinTableMeta.MY_VOLUME
        };
        ArrayList<CoinInfo> ret = new ArrayList<>();
        String selection = DbMeta.CoinTableMeta.INTEREST + "=1";
        Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columns, selection, null, null, null, DbMeta.CoinTableMeta.LIST_ORDER);
        if (c != null && c.moveToFirst()) {
            do {
                ret.add(
                        new CoinInfo(
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.COIN)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.CURRENCY)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.EXCHANGE)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.CHART_COINONE)),
                                c.getDouble(c.getColumnIndex(DbMeta.CoinTableMeta.MY_PRICE)),
                                c.getDouble(c.getColumnIndex(DbMeta.CoinTableMeta.MY_VOLUME))
                        )
                );
            } while (c.moveToNext());
            c.close();
        }
        Log.d(TAG, "getInterestingCoinList. size:" + ret.size());
        return ret;
    }

    public ArrayList<String> getAvailableCoinFullNameList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                DbMeta.CoinTableMeta.COIN,
                DbMeta.CoinTableMeta.CURRENCY,
                DbMeta.CoinTableMeta.EXCHANGE
        };
        ArrayList<String> ret = new ArrayList<>();
        Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columns, null, null, null, null, DbMeta.CoinTableMeta.LIST_ORDER);
        if (c != null && c.moveToFirst()) {
            do {
                String coinFullName = c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.COIN)) + "/"
                        + c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.CURRENCY)) + "("
                        + c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.EXCHANGE)) + ")";
                ret.add(coinFullName);
            } while (c.moveToNext());
            c.close();
        }
        Log.d(TAG, "getAvailableCoinFullNameList. size:" + ret.size());
        return ret;
    }

    public void updateHolding(String coin, String currency, String exchange, double avgPrice, double quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(DbMeta.CoinTableMeta.MY_PRICE, avgPrice);
        content.put(DbMeta.CoinTableMeta.MY_VOLUME, quantity);
        db.update(DbMeta.CoinTableMeta.TABLE_NAME, content,
                DbMeta.CoinTableMeta.COIN + "='" + coin + "' AND " +
                DbMeta.CoinTableMeta.CURRENCY + "='" + currency + "' AND " +
                DbMeta.CoinTableMeta.EXCHANGE + "='" + exchange + "' ", null);
    }

    public long getUpdateTime() {
        long ret = 0L;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(DbMeta.GlobalTableMeta.TABLE_NAME, null, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {

            do {
                ret = c.getLong(c.getColumnIndex(DbMeta.GlobalTableMeta.UPDATE_TIME));
            } while (c.moveToNext());
            c.close();
        }
        return ret;
    }

    public void setUpdateTime(long updateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(DbMeta.GlobalTableMeta.UPDATE_TIME, updateTime);
        db.update(DbMeta.GlobalTableMeta.TABLE_NAME, content, null, null);
    }

    public void updateCoinTable(ContentValues content, String coinFullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int updateRet = 0;
        if (coinFullName != null) {
            String[] values = coinFullName.split("/|\\(|\\)");
            Log.d(TAG, "updateCoinTable(). coinFullName: " + coinFullName + ", values: " + Arrays.toString(values));
            if (values.length == 3) {
                String coin = values[0];
                String currency = values[1];
                String exchange = values[2];
                Log.d(TAG, "updateCoinTable(). coin: " + coin + ", currency: " + currency + ", exchange: " + exchange);

                String whereClause =
                        DbMeta.CoinTableMeta.COIN + "='" + coin + "' AND " +
                        DbMeta.CoinTableMeta.CURRENCY + "='" + currency + "' AND " +
                        DbMeta.CoinTableMeta.EXCHANGE + "='" + exchange + "' ";

                updateRet = db.update(DbMeta.CoinTableMeta.TABLE_NAME, content, whereClause, null);
            } else {
                Log.e(TAG, "updateCoinTable(). unexpected coinFullName. values.length: " + values.length);
            }
        } else {
            updateRet = db.update(DbMeta.CoinTableMeta.TABLE_NAME, content, null, null);
        }
        Log.d(TAG, "updateCoinTable(). updateRet: " + updateRet);
    }
}
