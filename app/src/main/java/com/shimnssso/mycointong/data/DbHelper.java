package com.shimnssso.mycointong.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shimnssso.mycointong.Const;

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
                + DbMeta.CoinTableMeta.DEFAULT_LINK + " TEXT"
                + " )"
        );

        db.execSQL("CREATE TABLE " + DbMeta.InterestTableMeta.TABLE_NAME + " ("
                + DbMeta.InterestTableMeta.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DbMeta.InterestTableMeta.GROUP_ID + " INTEGER NOT NULL, "
                + DbMeta.InterestTableMeta.COIN_ID + " INTEGER NOT NULL, "
                + DbMeta.InterestTableMeta.ORDER_IN_GROUP + " INTEGER, "
                + DbMeta.InterestTableMeta.MY_PRICE + " REAL, "
                + DbMeta.InterestTableMeta.MY_VOLUME + " REAL, "
                + DbMeta.InterestTableMeta.LINK + " TEXT"
                + " )"
        );

        String[][] coinList = {
                {Const.Coin.BTC, Const.Currency.KRW, Const.Exchange.KORBIT, "https://coinone.co.kr/chart/?site=Korbit"},
                {Const.Coin.BTC, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/btckrw"},
                {Const.Coin.BTC, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=Coinone"},
                {Const.Coin.BTC, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/btcusd"},
                {Const.Coin.BTC, Const.Currency.CNY, Const.Exchange.OKCOIN, "https://cryptowat.ch/okcoin/btccny"},
                {Const.Coin.BTC, Const.Currency.JPY, Const.Exchange.BITFLYER, "https://cryptowat.ch/bitflyer/btcfxjpy"},

                {Const.Coin.BCH, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/bchkrw"},
                {Const.Coin.BCH, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneBCH"},
                {Const.Coin.BCH, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/bchusd"},

                {Const.Coin.ETH, Const.Currency.KRW, Const.Exchange.KORBIT, ""},
                {Const.Coin.ETH, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/ethkrw"},
                {Const.Coin.ETH, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneETH"},
                {Const.Coin.ETH, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/ethusd"},

                {Const.Coin.ETC, Const.Currency.KRW, Const.Exchange.KORBIT, ""},
                {Const.Coin.ETC, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/etckrw"},
                {Const.Coin.ETC, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneETC"},
                {Const.Coin.ETC, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/etcusd"},

                {Const.Coin.DASH, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/dashkrw"},

                {Const.Coin.LTC, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/ltckrw"},
                {Const.Coin.LTC, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/ltcusd"},
                {Const.Coin.LTC, Const.Currency.CNY, Const.Exchange.OKCOIN, "https://cryptowat.ch/okcoin/ltccny"},

                {Const.Coin.XRP, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/xrpkrw"},
                {Const.Coin.XRP, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneXRP"},
                {Const.Coin.XRP, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/xrpusd"},

                {Const.Coin.XMR, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/xmrkrw"},

                {Const.Coin.ZEC, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/zeckrw"},

                {Const.Coin.QTUM, Const.Currency.KRW, Const.Exchange.BITHUMB, ""},

        };

        String sql = "INSERT INTO " + DbMeta.CoinTableMeta.TABLE_NAME + " VALUES (null,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        for (String[] row : coinList) {
            Log.d(TAG, "row " + Arrays.toString(row));

            statement.clearBindings();
            statement.bindAllArgsAsStrings(row);
            statement.executeInsert();
        }

        Log.e(TAG, "after coin table");

        for (String[] row : coinList) {
            Log.e(TAG, "row " + Arrays.toString(row));

            Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, null,
                    DbMeta.CoinTableMeta.COIN + "='" + row[0] + "' AND " +
                    DbMeta.CoinTableMeta.CURRENCY + "='" + row[1] + "' AND " +
                    DbMeta.CoinTableMeta.EXCHANGE + "='" + row[2] + "' ",
                    null, null, null, null
            );
            if (c != null && c.moveToFirst()) {
                int coinId = c.getInt(c.getColumnIndex(DbMeta.CoinTableMeta.ID));
                String link = c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.DEFAULT_LINK));

                ContentValues contentValues = new ContentValues();
                contentValues.put(DbMeta.InterestTableMeta.GROUP_ID, 1);
                contentValues.put(DbMeta.InterestTableMeta.COIN_ID, coinId);
                contentValues.put(DbMeta.InterestTableMeta.LINK, link);

                long ret = db.insert(DbMeta.InterestTableMeta.TABLE_NAME, null, contentValues);
                Log.e(TAG, "ret " + ret);
            }
        }
        Log.e(TAG, "after interest table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade(). old: " + oldVersion + ", new: " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DbMeta.GlobalTableMeta.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbMeta.CoinTableMeta.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbMeta.InterestTableMeta.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<CoinInfo> getInterestingCoinList(int group) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                DbMeta.InterestTableMeta.TABLE_NAME + "." + DbMeta.InterestTableMeta.COIN_ID + " as " + DbMeta.InterestTableMeta.COIN_ID,
                DbMeta.CoinTableMeta.TABLE_NAME + "." + DbMeta.CoinTableMeta.COIN + " as " + DbMeta.CoinTableMeta.COIN,
                DbMeta.CoinTableMeta.TABLE_NAME + "." + DbMeta.CoinTableMeta.CURRENCY + " as " + DbMeta.CoinTableMeta.CURRENCY,
                DbMeta.CoinTableMeta.TABLE_NAME + "." + DbMeta.CoinTableMeta.EXCHANGE + " as " + DbMeta.CoinTableMeta.EXCHANGE,
                DbMeta.InterestTableMeta.TABLE_NAME + "." + DbMeta.InterestTableMeta.LINK + " as " + DbMeta.InterestTableMeta.LINK,
                DbMeta.InterestTableMeta.TABLE_NAME + "." + DbMeta.InterestTableMeta.MY_PRICE + " as " + DbMeta.InterestTableMeta.MY_PRICE,
                DbMeta.InterestTableMeta.TABLE_NAME + "." + DbMeta.InterestTableMeta.MY_VOLUME + " as " + DbMeta.InterestTableMeta.MY_VOLUME
        };
        ArrayList<CoinInfo> ret = new ArrayList<>();
        String selection = DbMeta.InterestTableMeta.TABLE_NAME + "." + DbMeta.InterestTableMeta.GROUP_ID + "=" + group;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DbMeta.CoinTableMeta.TABLE_NAME + " INNER JOIN " + DbMeta.InterestTableMeta.TABLE_NAME +
                " ON " + DbMeta.CoinTableMeta.TABLE_NAME + "." + DbMeta.CoinTableMeta.ID + "="
                + DbMeta.InterestTableMeta.TABLE_NAME + "." + DbMeta.InterestTableMeta.COIN_ID);
        Cursor c = queryBuilder.query(db, columns, selection, null, null, null, DbMeta.InterestTableMeta.ORDER_IN_GROUP + " ASC");
        if (c != null && c.moveToFirst()) {
            do {
                ret.add(
                        new CoinInfo(
                                c.getInt(c.getColumnIndex(DbMeta.InterestTableMeta.COIN_ID)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.COIN)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.CURRENCY)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.EXCHANGE)),
                                c.getString(c.getColumnIndex(DbMeta.InterestTableMeta.LINK)),
                                c.getDouble(c.getColumnIndex(DbMeta.InterestTableMeta.MY_PRICE)),
                                c.getDouble(c.getColumnIndex(DbMeta.InterestTableMeta.MY_VOLUME))
                        )
                );
            } while (c.moveToNext());
            c.close();
        }
        Log.d(TAG, "getInterestingCoinList. group: " + group + ", size:" + ret.size());
        return ret;
    }

    public ArrayList<CoinInfo> getAvailableCoinInfoList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                DbMeta.CoinTableMeta.ID,
                DbMeta.CoinTableMeta.COIN,
                DbMeta.CoinTableMeta.CURRENCY,
                DbMeta.CoinTableMeta.EXCHANGE
        };
        ArrayList<CoinInfo> ret = new ArrayList<>();
        Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columns, null, null, null, null, DbMeta.CoinTableMeta.EXCHANGE);
        if (c != null && c.moveToFirst()) {
            do {
                ret.add(
                        new CoinInfo(
                                c.getInt(c.getColumnIndex(DbMeta.CoinTableMeta.ID)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.COIN)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.CURRENCY)),
                                c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.EXCHANGE)),
                                null,
                                0.0f,
                                0.0f
                        )
                );
            } while (c.moveToNext());
            c.close();
        }
        Log.d(TAG, "getAvailableCoinInfoList. size:" + ret.size());
        return ret;
    }

    public String getFullNameFromId(int coinId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                DbMeta.CoinTableMeta.COIN,
                DbMeta.CoinTableMeta.CURRENCY,
                DbMeta.CoinTableMeta.EXCHANGE
        };
        String ret = null;
        Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columns, DbMeta.CoinTableMeta.ID + "=" + coinId, null, null, null, DbMeta.CoinTableMeta.EXCHANGE);
        if (c != null && c.moveToFirst()) {
            String coinFullName = c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.COIN)) + "/"
                    + c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.CURRENCY)) + "("
                    + c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.EXCHANGE)) + ")";
            ret = coinFullName;
            c.close();
        }
        Log.d(TAG, "getFullNameFromId. id: " + coinId + " , fullName: " + ret);
        return ret;
    }

    public void updateHolding(String coin, String currency, String exchange, double avgPrice, double quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, null,
                DbMeta.CoinTableMeta.COIN + "='" + coin + "' AND " +
                DbMeta.CoinTableMeta.CURRENCY + "='" + currency + "' AND " +
                DbMeta.CoinTableMeta.EXCHANGE + "='" + exchange + "' ",
                null, null, null, null
        );
        if (c != null && c.moveToFirst()) {
            int coinId = c.getInt(c.getColumnIndex(DbMeta.CoinTableMeta.ID));

            ContentValues content = new ContentValues();
            content.put(DbMeta.InterestTableMeta.MY_PRICE, avgPrice);
            content.put(DbMeta.InterestTableMeta.MY_VOLUME, quantity);

            db.update(DbMeta.InterestTableMeta.TABLE_NAME, content,
                    DbMeta.InterestTableMeta.COIN_ID + "=" + coinId, null);
        }
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

    public void upsertInterestRow(int group, int coinId, int orderInGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(DbMeta.InterestTableMeta.ORDER_IN_GROUP, orderInGroup);
        String whereClause = DbMeta.InterestTableMeta.GROUP_ID + "=" + group + " AND " +
                DbMeta.InterestTableMeta.COIN_ID + "=" + coinId;
        int updateRet = db.update(DbMeta.InterestTableMeta.TABLE_NAME, content, whereClause, null);
        Log.d(TAG, "upsertInterestRow(). updateRet: " + updateRet);
        if (updateRet == 0) {
            String[] columns = {
                DbMeta.CoinTableMeta.DEFAULT_LINK
            };
            Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columns, DbMeta.CoinTableMeta.ID + "=" + coinId, null, null, null, null);
            if (c != null && c.moveToFirst()) {
                String link = c.getString(c.getColumnIndex(DbMeta.CoinTableMeta.DEFAULT_LINK));

                content.put(DbMeta.InterestTableMeta.GROUP_ID, group);
                content.put(DbMeta.InterestTableMeta.COIN_ID, coinId);
                content.put(DbMeta.InterestTableMeta.LINK, link);
                long retId = db.insert(DbMeta.InterestTableMeta.TABLE_NAME, null, content);
                Log.d(TAG, "upsertInterestRow(). insetRet: " + retId);
            }
        }
    }

    public void removeDeletedRow(int group, int OutOrderIndex) {
        Log.d(TAG, "removeDeletedRow(). group: " + group + ", OutOrderIndex: " + OutOrderIndex);
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = DbMeta.InterestTableMeta.GROUP_ID + "=" + group + " AND " +
                DbMeta.InterestTableMeta.ORDER_IN_GROUP + ">=" + OutOrderIndex + " OR " +
                DbMeta.InterestTableMeta.ORDER_IN_GROUP + "=0 OR " +
                DbMeta.InterestTableMeta.ORDER_IN_GROUP + " IS NULL";
        int removeCount = db.delete(DbMeta.InterestTableMeta.TABLE_NAME, whereClause, null);
        Log.d(TAG, "removeDeletedRow(). removeCount: " + removeCount);
    }

}
