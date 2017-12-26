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
import com.shimnssso.mycointong.exchangerate.FinanceHelper;

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
                + DbMeta.GlobalTableMeta.UPDATE_TIME + " INTEGER, "
                + DbMeta.GlobalTableMeta.USD_KRW + " REAL, "
                + DbMeta.GlobalTableMeta.USD_JPY + " REAL, "
                + DbMeta.GlobalTableMeta.UPDATE_TIME_EXCHANGE_RATE + " INTEGER"
                + " )"
        );
        db.execSQL("INSERT INTO " + DbMeta.GlobalTableMeta.TABLE_NAME + " VALUES (null,0,1082.38,112.815,1513694042183)");

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
                {Const.Coin.BTC, Const.Currency.KRW, Const.Exchange.KORBIT, "https://www.korbit.co.kr/market/btc_krw"},
                {Const.Coin.BTC, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/btckrw"},
                {Const.Coin.BTC, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=Coinone"},
                {Const.Coin.BTC, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/btcusd"},
                {Const.Coin.BTC, Const.Currency.JPY, Const.Exchange.BITFLYER, "https://cryptowat.ch/bitflyer/btcjpy"},  // db version 6

                {Const.Coin.BCH, Const.Currency.KRW, Const.Exchange.KORBIT, "https://www.korbit.co.kr/market/bch_krw"},
                {Const.Coin.BCH, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/bchkrw"},
                {Const.Coin.BCH, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneBCH"},
                {Const.Coin.BCH, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/bchusd"},

                {Const.Coin.BTG, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/btgkrw"},  // db version 2
                {Const.Coin.BTG, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/btgusd"},  // db version 3

                {Const.Coin.ETH, Const.Currency.KRW, Const.Exchange.KORBIT, "https://www.korbit.co.kr/market/eth_krw"},
                {Const.Coin.ETH, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/ethkrw"},
                {Const.Coin.ETH, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneETH"},
                {Const.Coin.ETH, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/ethusd"},

                {Const.Coin.ETC, Const.Currency.KRW, Const.Exchange.KORBIT, "https://www.korbit.co.kr/market/etc_krw"},
                {Const.Coin.ETC, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/etckrw"},
                {Const.Coin.ETC, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneETC"},
                {Const.Coin.ETC, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/etcusd"},

                {Const.Coin.DASH, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/dashkrw"},
                {Const.Coin.DASH, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/dashusd"},

                {Const.Coin.LTC, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/ltckrw"},
                {Const.Coin.LTC, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneLTC"}, // db version 2
                {Const.Coin.LTC, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/ltcusd"},

                {Const.Coin.XRP, Const.Currency.KRW, Const.Exchange.KORBIT, "https://www.korbit.co.kr/market/xrp_krw"},
                {Const.Coin.XRP, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/xrpkrw"},
                {Const.Coin.XRP, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneXRP"},
                {Const.Coin.XRP, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/xrpusd"},

                {Const.Coin.XMR, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/xmrkrw"},
                {Const.Coin.XMR, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/xmrusd"},

                {Const.Coin.ZEC, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/zeckrw"},
                {Const.Coin.ZEC, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/zecusd"},

                {Const.Coin.QTUM, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/qtumkrw"},
                {Const.Coin.QTUM, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneQTUM"},
                {Const.Coin.QTUM, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/qtmusd"},

                {Const.Coin.IOTA, Const.Currency.KRW, Const.Exchange.COINONE, "https://coinone.co.kr/chart/?site=CoinoneIOTA"}, // db version 2
                {Const.Coin.IOTA, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/iotusd"},  // db version 3

                {Const.Coin.EOS, Const.Currency.KRW, Const.Exchange.BITHUMB, "https://cryptowat.ch/bithumb/eoskrw"}, // db version 4
                {Const.Coin.EOS, Const.Currency.USD, Const.Exchange.BITFINEX, "https://cryptowat.ch/bitfinex/eosusd"},  // db version 4
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
        // 1 -> 2
        if (newVersion <= 2) {
            // remove OKCoin exchange
            String[] columns = {DbMeta.CoinTableMeta.ID};
            ArrayList<Integer> coinList = new ArrayList<>();
            Cursor c = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columns, DbMeta.CoinTableMeta.EXCHANGE + "='" + Const.Exchange.OKCOIN +"'", null, null, null, null);
            if (c != null && c.moveToFirst()) {
                do {
                    coinList.add(c.getInt(0));
                } while (c.moveToNext());
                c.close();
            }
            int listSize = coinList.size();
            if (listSize > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < listSize; i++) {
                    sb.append(DbMeta.InterestTableMeta.COIN_ID + "=" + coinList.get(i));
                    if (i < listSize - 1) {
                        sb.append(" OR ");
                    }
                }
                Log.d(TAG, "onUpgrade 1 to 2. where condition: " + sb);
                int deleteInterestTableRet = db.delete(DbMeta.InterestTableMeta.TABLE_NAME, sb.toString(), null);
                Log.i(TAG, "onUpgrade 1 to 2. deleteInterestTableRet: " + deleteInterestTableRet);
            }
            int deleteCoinTableRet = db.delete(DbMeta.CoinTableMeta.TABLE_NAME, DbMeta.CoinTableMeta.EXCHANGE + "='" + Const.Exchange.OKCOIN +"'",null);
            Log.i(TAG, "onUpgrade 1 to 2. deleteCoinTableRet: " + deleteCoinTableRet);

            // Fill QTUM(Bithumb) url
            String[] columnsQtum = {DbMeta.CoinTableMeta.ID};
            int qtumId = -1;
            Cursor cursor = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columnsQtum,
                    DbMeta.CoinTableMeta.EXCHANGE + "='" + Const.Exchange.BITHUMB +"' AND " +
                    DbMeta.CoinTableMeta.COIN + "='" + Const.Coin.QTUM + "'"
                , null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                qtumId = cursor.getInt(0);
                cursor.close();
            }
            if (qtumId > 0) {
                ContentValues cvForInterestTable = new ContentValues();
                cvForInterestTable.put(DbMeta.InterestTableMeta.LINK, "https://cryptowat.ch/bithumb/qtumkrw");
                int updateInterestTableRet = db.update(DbMeta.InterestTableMeta.TABLE_NAME, cvForInterestTable, DbMeta.InterestTableMeta.COIN_ID + "=" + qtumId, null);
                Log.i(TAG, "onUpgrade 1 to 2. updateInterestTableRet: " + updateInterestTableRet);
            }

            ContentValues cv = new ContentValues();
            cv.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://cryptowat.ch/bithumb/qtumkrw");
            int updateCoinTableRet = db.update(DbMeta.CoinTableMeta.TABLE_NAME, cv,
                    DbMeta.CoinTableMeta.COIN + "='" + Const.Coin.QTUM + "' AND " +
                    DbMeta.CoinTableMeta.EXCHANGE + "='" + Const.Exchange.BITHUMB +"'"
                , null);
            Log.i(TAG, "onUpgrade 1 to 2. updateCoinTableRet: " + updateCoinTableRet);

            // add BTG(Bithumb)
            ContentValues cvForBtg = new ContentValues();
            cvForBtg.put(DbMeta.CoinTableMeta.COIN, Const.Coin.BTG);
            cvForBtg.put(DbMeta.CoinTableMeta.CURRENCY, Const.Currency.KRW);
            cvForBtg.put(DbMeta.CoinTableMeta.EXCHANGE, Const.Exchange.BITHUMB);
            cvForBtg.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://cryptowat.ch/bithumb/btgkrw");
            long insertRet = db.insert(DbMeta.CoinTableMeta.TABLE_NAME, null, cvForBtg);
            Log.i(TAG, "onUpgrade 1 to 2. BTG. insertRet: " + insertRet);

            // add IOTA, LTC (Coinone)
            ContentValues cvForCoinone = new ContentValues();
            cvForCoinone.put(DbMeta.CoinTableMeta.COIN, Const.Coin.IOTA);
            cvForCoinone.put(DbMeta.CoinTableMeta.CURRENCY, Const.Currency.KRW);
            cvForCoinone.put(DbMeta.CoinTableMeta.EXCHANGE, Const.Exchange.COINONE);
            cvForCoinone.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://coinone.co.kr/chart/?site=CoinoneIOTA");
            insertRet = db.insert(DbMeta.CoinTableMeta.TABLE_NAME, null, cvForCoinone);
            Log.i(TAG, "onUpgrade 1 to 2. IOTA. insertRet: " + insertRet);

            cvForCoinone.put(DbMeta.CoinTableMeta.COIN, Const.Coin.LTC);
            cvForCoinone.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://coinone.co.kr/chart/?site=CoinoneLTC");
            insertRet = db.insert(DbMeta.CoinTableMeta.TABLE_NAME, null, cvForCoinone);
            Log.i(TAG, "onUpgrade 1 to 2. LTC. insertRet: " + insertRet);
        }


        // 2 -> 3
        if (newVersion <= 3) {
            // add BTG, IOTA (Bitfinex)
            ContentValues cvForFinex = new ContentValues();
            cvForFinex.put(DbMeta.CoinTableMeta.COIN, Const.Coin.BTG);
            cvForFinex.put(DbMeta.CoinTableMeta.CURRENCY, Const.Currency.USD);
            cvForFinex.put(DbMeta.CoinTableMeta.EXCHANGE, Const.Exchange.BITFINEX);
            cvForFinex.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://cryptowat.ch/bitfinex/btgusd");
            long insertRet = db.insert(DbMeta.CoinTableMeta.TABLE_NAME, null, cvForFinex);
            Log.i(TAG, "onUpgrade 2 to 3. BTG. insertRet: " + insertRet);

            cvForFinex.put(DbMeta.CoinTableMeta.COIN, Const.Coin.IOTA);
            cvForFinex.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://cryptowat.ch/bitfinex/iotusd");
            insertRet = db.insert(DbMeta.CoinTableMeta.TABLE_NAME, null, cvForFinex);
            Log.i(TAG, "onUpgrade 2 to 3. IOTA. insertRet: " + insertRet);
        }

        // 3 -> 4
        if (newVersion <= 4) {
            // add EOS (Bithumb)
            ContentValues cvForEos = new ContentValues();
            cvForEos.put(DbMeta.CoinTableMeta.COIN, Const.Coin.EOS);
            cvForEos.put(DbMeta.CoinTableMeta.CURRENCY, Const.Currency.KRW);
            cvForEos.put(DbMeta.CoinTableMeta.EXCHANGE, Const.Exchange.BITHUMB);
            cvForEos.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://cryptowat.ch/bithumb/eoskrw");
            long insertRet = db.insert(DbMeta.CoinTableMeta.TABLE_NAME, null, cvForEos);
            Log.i(TAG, "onUpgrade 3 to 4. EOS(Bithumb). insertRet: " + insertRet);

            // add EOS (Bitfinex)
            ContentValues cvForEosFinex = new ContentValues();
            cvForEosFinex.put(DbMeta.CoinTableMeta.COIN, Const.Coin.EOS);
            cvForEosFinex.put(DbMeta.CoinTableMeta.CURRENCY, Const.Currency.USD);
            cvForEosFinex.put(DbMeta.CoinTableMeta.EXCHANGE, Const.Exchange.BITFINEX);
            cvForEosFinex.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://cryptowat.ch/bitfinex/eosusd");
            insertRet = db.insert(DbMeta.CoinTableMeta.TABLE_NAME, null, cvForEosFinex);
            Log.i(TAG, "onUpgrade 3 to 4. EOS(Finex). insertRet: " + insertRet);
        }

        // 4 -> 5
        if (newVersion <= 5) {
            db.execSQL("ALTER TABLE " + DbMeta.GlobalTableMeta.TABLE_NAME + " ADD " + DbMeta.GlobalTableMeta.USD_KRW + " REAL DEFAULT 1082.38;");
            db.execSQL("ALTER TABLE " + DbMeta.GlobalTableMeta.TABLE_NAME + " ADD " + DbMeta.GlobalTableMeta.USD_JPY + " REAL DEFAULT 112.815;");
            db.execSQL("ALTER TABLE " + DbMeta.GlobalTableMeta.TABLE_NAME + " ADD " + DbMeta.GlobalTableMeta.UPDATE_TIME_EXCHANGE_RATE + " INTEGER DEFAULT 1513694042183;");
        }

        // 5 -> 6
        if (newVersion <= 6) {
            String[] columnsQtum = {DbMeta.CoinTableMeta.ID};
            int btcJpyId = -1;
            Cursor cursor = db.query(DbMeta.CoinTableMeta.TABLE_NAME, columnsQtum,
                    DbMeta.CoinTableMeta.EXCHANGE + "='" + Const.Exchange.BITFLYER +"' AND " +
                            DbMeta.CoinTableMeta.COIN + "='" + Const.Coin.BTC + "'"
                    , null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                btcJpyId = cursor.getInt(0);
                cursor.close();
            }
            if (btcJpyId > 0) {
                // Update Interest Table
                ContentValues cvForInterestTable = new ContentValues();
                cvForInterestTable.put(DbMeta.InterestTableMeta.LINK, "https://cryptowat.ch/bitflyer/btcjpy");
                int updateInterestTableRet = db.update(DbMeta.InterestTableMeta.TABLE_NAME, cvForInterestTable, DbMeta.InterestTableMeta.COIN_ID + "=" + btcJpyId, null);
                Log.i(TAG, "onUpgrade 5 to 6. updateInterestTableRet: " + updateInterestTableRet);

                // Update Coin Table
                ContentValues cvForCoinTable = new ContentValues();
                cvForCoinTable.put(DbMeta.CoinTableMeta.DEFAULT_LINK, "https://cryptowat.ch/bitflyer/btcjpy");
                int updateCoinTableRet = db.update(DbMeta.CoinTableMeta.TABLE_NAME, cvForCoinTable, DbMeta.CoinTableMeta.ID + "=" + btcJpyId, null);
                Log.i(TAG, "onUpgrade 5 to 6. updateCoinTableRet: " + updateCoinTableRet);
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
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

    public void resetOrder(int group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(DbMeta.InterestTableMeta.ORDER_IN_GROUP, 0);
        String whereClause = DbMeta.InterestTableMeta.GROUP_ID + "=" + group;
        int updateRet = db.update(DbMeta.InterestTableMeta.TABLE_NAME, content, whereClause, null);
        Log.d(TAG, "resetOrder(). updateRet: " + updateRet);
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

    public void loadExchangeRate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(DbMeta.GlobalTableMeta.TABLE_NAME, null, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            float usdKrw = c.getFloat(c.getColumnIndex(DbMeta.GlobalTableMeta.USD_KRW));
            FinanceHelper.setUsdKrw(usdKrw);
            float usdJpy = c.getFloat(c.getColumnIndex(DbMeta.GlobalTableMeta.USD_JPY));
            FinanceHelper.setUsdJpy(usdJpy);
            long updateTimeExchangeRate = c.getLong(c.getColumnIndex(DbMeta.GlobalTableMeta.UPDATE_TIME_EXCHANGE_RATE));
            FinanceHelper.setUpdateTime(updateTimeExchangeRate);
            c.close();
        } else {
            Log.e(TAG, "readExchangeRate(). unexpected condition");
        }
    }

    public void updateExchangeRate() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(DbMeta.GlobalTableMeta.USD_KRW, FinanceHelper.getUsdKrw());
        content.put(DbMeta.GlobalTableMeta.USD_JPY, FinanceHelper.getUsdJpy());
        content.put(DbMeta.GlobalTableMeta.UPDATE_TIME_EXCHANGE_RATE, FinanceHelper.getUpdateTime());
        int ret = db.update(DbMeta.GlobalTableMeta.TABLE_NAME, content, null, null);
        Log.d(TAG, "updateExchangeRate(). ret: " + ret);
    }

}
