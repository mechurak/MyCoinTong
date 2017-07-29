package com.shimnssso.mycointong.network;

import android.os.AsyncTask;
import android.util.Log;

import com.shimnssso.mycointong.Constant;
import com.shimnssso.mycointong.ListViewAdapter;
import com.shimnssso.mycointong.ListViewItem;

import org.json.JSONException;
import org.json.JSONObject;

public class CryptowatchClient extends AsyncTask<Void, Void, String> {
    private static final String TAG = "CryptowatchClient";
    private static final String TICKER_URL = "https://api.cryptowat.ch/markets/summaries";

    // parameter
    private static String RESULT = "result";

    private static String BITFINEX_BTCUSD = "bitfinex:btcusd";
    private static String OKCOIN_BTCCNY = "okcoin:btccny";
    private static String BITFLYER_BTCJPY = "bitflyer:btcjpy";

    private static String VOLUME = "volume";
    private static String PRICE = "price";

    // child of "price"
    private static String LAST = "last";
    private static String HIGH = "high";
    private static String LOW = "low";
    private static String CHANGE = "change";

    // child of "change"
    private static String ABSOLUTE = "absolute";

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
            Log.e(TAG, "s == null");
            return;
        }

        JSONObject responseObject;
        try {
            responseObject = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (responseObject.has(RESULT)) {
            responseObject = responseObject.optJSONObject(RESULT);
        } else {
            Log.e(TAG, "result doesn't exist");
            return;
        }

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;

        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.BTC, Constant.Currency.USD, Constant.Exchange.BITFINEX);
        if (responseObject.has(BITFINEX_BTCUSD)) {
            JSONObject coinObejct = responseObject.optJSONObject(BITFINEX_BTCUSD);
            updateItemValues(null, coinObejct);
        }

        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.BTC, Constant.Currency.CNY, Constant.Exchange.OKCOIN);
        if (responseObject.has(OKCOIN_BTCCNY)) {
            JSONObject coinObejct = responseObject.optJSONObject(OKCOIN_BTCCNY);
            updateItemValues(null, coinObejct);
        }

        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.BTC, Constant.Currency.JPY, Constant.Exchange.BITFLYER);
        if (responseObject.has(BITFLYER_BTCJPY)) {
            JSONObject coinObejct = responseObject.optJSONObject(BITFLYER_BTCJPY);
            updateItemValues(null, coinObejct);
        }

        adapterInstance.notifyDataSetChanged();
    }

    private void updateItemValues(ListViewItem item, JSONObject coinObject) {
        try {
            double volume = coinObject.getDouble(VOLUME);
            JSONObject priceObject = coinObject.optJSONObject(PRICE);
            if (priceObject != null) {
                double highPrice = priceObject.getDouble(HIGH);
                double lowPrice = priceObject.getDouble(LOW);
                double curPrice = priceObject.getDouble(LAST);
                double absoluteChange = priceObject.getJSONObject(CHANGE).getDouble(ABSOLUTE);
                double openPrice = curPrice - absoluteChange;

                Log.d(TAG, "O:" + openPrice + ", H: " + highPrice + ", L: " + lowPrice + ", C: " + curPrice + ", V: " + volume);
            }

            //item.setPrice(openPrice, highPrice, lowPrice, curPrice, volume);
            //Log.d(TAG, item.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
