package com.shimnssso.mycointong.network;

import android.os.AsyncTask;
import android.util.Log;

import com.shimnssso.mycointong.Const;
import com.shimnssso.mycointong.ListViewAdapter;
import com.shimnssso.mycointong.ListViewItem;

import org.json.JSONException;
import org.json.JSONObject;

public class CryptowatchClient extends AsyncTask<Void, Void, JSONObject> {
    private static final String TAG = "CryptowatchClient";
    private static final String TICKER_URL = "https://api.cryptowat.ch/markets/";

    // parameter
    private static final String OKCOIN_BTC_CNY = "okcoin/btccny/summary";
    private static final String OKCOIN_LTC_CNY = "okcoin/ltccny/summary";
    private static final String BITFLYER_BTC_JPY = "bitflyer/btcjpy/summary";

    // response
    private static final String RESULT = "result";

    private static final String VOLUME = "volume";
    private static final String PRICE = "price";

    // child of "price"
    private static final String LAST = "last";
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String CHANGE = "change";

    // child of "change"
    private static final String ABSOLUTE = "absolute";

    private TickerListener mListener;
    public CryptowatchClient(TickerListener listener) {
        mListener = listener;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        Log.d(TAG, "doInBackground()");

        JSONObject mergedRetJson = new JSONObject();
        String retString;
        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;
        JSONObject curRetJson;

        try {
            // OKCOIN_BTC_CNY
            curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BTC, Const.Currency.CNY, Const.Exchange.OKCOIN);
            if (curItem != null) {
                retString = NetworkUtil.request(TICKER_URL + OKCOIN_BTC_CNY);
                curRetJson = new JSONObject(retString);
                mergedRetJson.put(OKCOIN_BTC_CNY, curRetJson);
            }

            // OKCOIN_LTC_CNY
            curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.LTC, Const.Currency.CNY, Const.Exchange.OKCOIN);
            if (curItem != null) {
                retString = NetworkUtil.request(TICKER_URL + OKCOIN_LTC_CNY);
                curRetJson = new JSONObject(retString);
                mergedRetJson.put(OKCOIN_LTC_CNY, curRetJson);
            }

            // BITFLYER_BTC_JPY
            curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BTC, Const.Currency.JPY, Const.Exchange.BITFLYER);
            if (curItem != null) {
                retString = NetworkUtil.request(TICKER_URL + BITFLYER_BTC_JPY);
                curRetJson = new JSONObject(retString);
                mergedRetJson.put(BITFLYER_BTC_JPY, curRetJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mergedRetJson;
    }

    @Override
    protected void onPostExecute(JSONObject mergedRetJson) {
        super.onPostExecute(mergedRetJson);
        Log.d(TAG, "onPostExecute(). response: " + mergedRetJson);
        if (mergedRetJson == null) {
            Log.e(TAG, "mergedRetJson == null");
            mListener.OnRefreshResult(Const.Exchange.CRYPTOWATCH, 0);
            return;
        }

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;
        JSONObject responseObject;

        // OKCOIN_BTC_CNY
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BTC, Const.Currency.CNY, Const.Exchange.OKCOIN);
        if (curItem != null && mergedRetJson.has(OKCOIN_BTC_CNY)) {
            responseObject = mergedRetJson.optJSONObject(OKCOIN_BTC_CNY);
            updateItemValues(curItem, responseObject);
        }

        // OKCOIN_LTC_CNY
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.LTC, Const.Currency.CNY, Const.Exchange.OKCOIN);
        if (curItem != null && mergedRetJson.has(OKCOIN_LTC_CNY)) {
            responseObject = mergedRetJson.optJSONObject(OKCOIN_LTC_CNY);
            updateItemValues(curItem, responseObject);
        }

        // BITFLYER_BTC_JPY
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BTC, Const.Currency.JPY, Const.Exchange.BITFLYER);
        if (curItem != null && mergedRetJson.has(BITFLYER_BTC_JPY)) {
            responseObject = mergedRetJson.optJSONObject(BITFLYER_BTC_JPY);
            updateItemValues(curItem, responseObject);
        }

        adapterInstance.notifyDataSetChanged();
        mListener.OnRefreshResult(Const.Exchange.CRYPTOWATCH, 1);
    }

    private void updateItemValues(ListViewItem item, JSONObject responseObject) {
        try {
            JSONObject coinObject;
            if (responseObject.has(RESULT)) {
                coinObject = responseObject.optJSONObject(RESULT);
            } else {
                Log.e(TAG, "result doesn't exist");
                return;
            }

            double volume = coinObject.getDouble(VOLUME);
            JSONObject priceObject = coinObject.optJSONObject(PRICE);
            if (priceObject != null) {
                double highPrice = priceObject.getDouble(HIGH);
                double lowPrice = priceObject.getDouble(LOW);
                double curPrice = priceObject.getDouble(LAST);
                double absoluteChange = priceObject.getJSONObject(CHANGE).getDouble(ABSOLUTE);
                double openPrice = curPrice - absoluteChange;

                item.setPrice(openPrice, highPrice, lowPrice, curPrice, volume);
                Log.d(TAG, item.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
