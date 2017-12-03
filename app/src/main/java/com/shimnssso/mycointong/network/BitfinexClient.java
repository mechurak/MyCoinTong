package com.shimnssso.mycointong.network;

import android.os.AsyncTask;
import android.util.Log;

import com.shimnssso.mycointong.Const;
import com.shimnssso.mycointong.ListViewAdapter;
import com.shimnssso.mycointong.ListViewItem;

import org.json.JSONArray;
import org.json.JSONException;

public class BitfinexClient extends AsyncTask<Void, Void, String> {
    private static final String TAG = "BitfinexClient";
    private static final String TICKER_URL = "https://api.bitfinex.com/v2/tickers?symbols=tBTCUSD,tBCHUSD,tLTCUSD,tETHUSD,tETCUSD,tXRPUSD,tZECUSD,tXMRUSD,tDSHUSD,tQTMUSD,tBTGUSD,tIOTUSD";
    private static final String MY_CURRENCY = Const.Currency.USD;
    private static final String MY_EXCHANGE = Const.Exchange.BITFINEX;

    // parameter
    private static String BTCUSD = "tBTCUSD";
    private static String BCHUSD = "tBCHUSD";
    private static String LTCUSD = "tLTCUSD";
    private static String ETHUSD = "tETHUSD";
    private static String ETCUSD = "tETCUSD";
    private static String XRPUSD = "tXRPUSD";
    private static String ZECUSD = "tZECUSD";
    private static String XMRUSD = "tXMRUSD";
    private static String DSHUSD = "tDSHUSD";
    private static String QTMUSD = "tQTMUSD";

    private static String BTGUSD = "tBTGUSD";
    private static String IOTAUSD = "tIOTUSD";

    // result
    private static String TIME_STAMP = "timestamp";
    private static String VOLUME = "volume";
    private static String LAST_PRICE = "last_price";
    private static String HIGH = "high";
    private static String LOW = "low";
    private static String FIRST = "first";

    private TickerListener mListener;
    public BitfinexClient(TickerListener listener) {
        mListener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.d(TAG, "doInBackground()");
        return NetworkUtil.request(TICKER_URL);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute(). response: " + s);
        if (s == null) {
            Log.e(TAG, "mergedRetJson == null");
            mListener.OnRefreshResult(Const.Exchange.BITFINEX, 0);
            return;
        }

        JSONArray responseArray;
        try {
            responseArray = new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
            mListener.OnRefreshResult(Const.Exchange.BITFINEX, 0);
            return;
        }

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;

        Log.d(TAG, "responseArray length: " + responseArray.length());

        for (int i = 0; i < responseArray.length(); i++) {
            JSONArray curArray = responseArray.optJSONArray(i);
            if (curArray != null) {
                String coinFullName = curArray.optString(0);
                if (coinFullName == null || coinFullName.length() < 4) continue;
                Log.d(TAG, "coinFullName: " + coinFullName);
                String coin = coinFullName.substring(1, 4);
                Log.d(TAG, "coin: " + coin);
                if (coin.equals("QTM")) {
                    coin = "QTUM";
                } else if (coin.equals("DSH")) {
                    coin = "DASH";
                } else if (coin.equals("IOT")) {
                    coin = "IOTA";
                }
                double dailyChange = curArray.optDouble(5);
                double lastPrice = curArray.optDouble(7);
                double volume = curArray.optDouble(8);
                double high = curArray.optDouble(9);
                double low = curArray.optDouble(10);

                curItem = (ListViewItem) adapterInstance.getItemByName(coin, MY_CURRENCY, MY_EXCHANGE);
                if (curItem != null) {
                    curItem.setPrice(lastPrice-dailyChange, high, low, lastPrice, volume);
                    Log.d(TAG, curItem.toString());
                }
            }
        }

        adapterInstance.notifyDataSetChanged();
        mListener.OnRefreshResult(Const.Exchange.BITFINEX, 1);
    }
}

