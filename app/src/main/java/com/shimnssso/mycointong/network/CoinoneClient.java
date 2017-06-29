package com.shimnssso.mycointong.network;

import android.os.AsyncTask;
import android.util.Log;

import com.shimnssso.mycointong.Constant;
import com.shimnssso.mycointong.ListViewAdapter;
import com.shimnssso.mycointong.ListViewItem;

import org.json.JSONException;
import org.json.JSONObject;

public class CoinoneClient extends AsyncTask<Void, Void, String> {
    private static final String TAG = "CoinoneClient";
    private static final String TICKER_URL = "https://api.coinone.co.kr/ticker?currency=all";

    // parameter
    // currency    btc(default), eth, etc, xrp, all
    private static String RESULT = "result";
    private static String RESULT_SUCCESS = "success";

    private static String ERROR_CODE = "errorCode";
    private static String TIME_STAMP = "timestamp";

    private static String BTC = "btc";
    private static String ETH = "eth";
    private static String ETC = "etc";
    private static String XRP = "xrp";

    private static String VOLUME = "volume";
    private static String LAST = "last";
    private static String HIGH = "high";
    private static String CURRENCY = "currency";
    private static String LOW = "low";
    private static String FIRST = "first";

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

        String status = null;
        if (responseObject.has(RESULT)) {
            status = responseObject.optString(RESULT, "FAIL");
        }
        if (status == null || !status.equals(RESULT_SUCCESS)) {
            Log.e(TAG, "status: " + status);
            return;
        }

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;

        // BTC
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.CoinName.BTC_COINONE);
        if (curItem != null && responseObject.has(BTC)) {
            JSONObject coinObject = responseObject.optJSONObject(BTC);
            updateItemValues(curItem, coinObject);
        }

        // ETH
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.CoinName.ETH_COINONE);
        if (curItem != null && responseObject.has(ETH)) {
            JSONObject coinObject = responseObject.optJSONObject(ETH);
            updateItemValues(curItem, coinObject);
        }

        // ETC
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.CoinName.ETC_COINONE);
        if (curItem != null && responseObject.has(ETC)) {
            JSONObject coinObject = responseObject.optJSONObject(ETC);
            updateItemValues(curItem, coinObject);
        }

        // XRP
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.CoinName.XRP_COINONE);
        if (curItem != null && responseObject.has(XRP)) {
            JSONObject coinObject = responseObject.optJSONObject(XRP);
            updateItemValues(curItem, coinObject);
        }

        if (responseObject.has(TIME_STAMP)) {
            long timestamp = responseObject.optLong(TIME_STAMP);
            Log.d(TAG, "onPostExecute(). timestamp: " + timestamp + ", curTime: " + System.currentTimeMillis());
        }
        adapterInstance.notifyDataSetChanged();
    }

    private void updateItemValues(ListViewItem item, JSONObject coinObject) {
        try {
            int openPrice = coinObject.getInt(FIRST);
            int highPrice = coinObject.getInt(HIGH);
            int lowPrice = coinObject.getInt(LOW);
            int curPrice = coinObject.getInt(LAST);
            double volume = coinObject.getDouble(VOLUME);

            item.setPrice(openPrice, highPrice, lowPrice, curPrice, volume);
            Log.d(TAG, item.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
