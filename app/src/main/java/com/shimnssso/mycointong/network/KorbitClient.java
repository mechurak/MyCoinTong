package com.shimnssso.mycointong.network;

import android.os.AsyncTask;
import android.util.Log;

import com.shimnssso.mycointong.Constant;
import com.shimnssso.mycointong.ListViewAdapter;
import com.shimnssso.mycointong.ListViewItem;

import org.json.JSONException;
import org.json.JSONObject;

public class KorbitClient extends AsyncTask<Void, Void, String> {
    private static final String TAG = "KorbitClient";
    private static final String TICKER_URL = "https://api.korbit.co.kr/v1/ticker/detailed";

    // parameter
    // currency_pair	btc_krw (default), etc_krw, eth_krw, xrp_krw
    private static String TIME_STAMP = "timestamp";
    private static String LAST = "last";
    private static String BID = "bid";
    private static String ASK = "ask";
    private static String LOW = "low";
    private static String HIGH = "high";
    private static String VOLUME = "volume";
    private static String CHANGE = "change";

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

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;

        // BTC
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.CoinName.BTC_KORBIT);
        if (curItem != null && responseObject.has(TIME_STAMP)) {
            updateItemValues(curItem, responseObject);
        }

        adapterInstance.notifyDataSetChanged();
    }

    private void updateItemValues(ListViewItem item, JSONObject coinObject) {
        try {
            int openPrice = coinObject.getInt(LAST) - coinObject.getInt(CHANGE);
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