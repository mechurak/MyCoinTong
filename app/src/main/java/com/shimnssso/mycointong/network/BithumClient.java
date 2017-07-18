package com.shimnssso.mycointong.network;

import android.os.AsyncTask;
import android.util.Log;

import com.shimnssso.mycointong.Constant;
import com.shimnssso.mycointong.ListViewAdapter;
import com.shimnssso.mycointong.ListViewItem;

import org.json.JSONException;
import org.json.JSONObject;

public class BithumClient extends AsyncTask<Void, Void, String> {
    private static final String TAG = "BithumClient";
    private static final String TICKER_URL = "https://api.bithumb.com/public/ticker/all";
    private static final String MY_CURRENCY = Constant.Currency.KRW;
    private static final String MY_EXCHANGE = Constant.Exchange.BITHUMB;

    // {currency} BTC, ETH, DASH, LTC, ETC, XRP (default: BTC), ALL
    private static String STATUS = "status";
    private static String STATUS_SUCCESS = "0000";

    private static String DATA = "data";

    private static String BTC = "BTC";
    private static String ETH = "ETH";
    private static String DASH = "DASH";
    private static String LTC = "LTC";
    private static String ETC = "ETC";
    private static String XRP = "XRP";
    private static String DATE = "date";

    private static String OPENING_PRICE = "opening_price";
    private static String CLOSING_PRICE = "closing_price";
    private static String MIN_PRICE = "min_price";
    private static String MAX_PRICE = "max_price";
    private static String AVERAGE_PRICE = "average_price";
    private static String UNITS_TRADED = "units_traded";
    private static String VOLUME_1DAY = "volume_1day";
    private static String VOLUME_7DAY = "volume_7day";
    private static String BUY_PRICE = "buy_price";
    private static String SELL_PRICE = "sell_price";

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
        if (responseObject.has(STATUS)) {
            status = responseObject.optString(STATUS, "FAIL");
        }
        if (status == null || !status.equals(STATUS_SUCCESS)) {
            Log.e(TAG, "status: " + status);
            return;
        }

        JSONObject dataObject = null;
        if (responseObject.has(DATA)) {
            dataObject = responseObject.optJSONObject(DATA);
        }
        if (dataObject == null) {
            Log.e(TAG, "dataObject == null");
            return;
        }

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;

        // BTC
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.BTC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(BTC)) {
            JSONObject coinObject = dataObject.optJSONObject(BTC);
            updateItemValues(curItem, coinObject);
        }

        // ETH
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.ETH, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(ETH)) {
            JSONObject coinObject = dataObject.optJSONObject(ETH);
            updateItemValues(curItem, coinObject);
        }

        // DASH
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.DASH, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(DASH)) {
            JSONObject coinObject = dataObject.optJSONObject(DASH);
            updateItemValues(curItem, coinObject);
        }

        // LTC
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.LTC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(LTC)) {
            JSONObject coinObject = dataObject.optJSONObject(LTC);
            updateItemValues(curItem, coinObject);
        }

        // ETC
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.ETC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(ETC)) {
            JSONObject coinObject = dataObject.optJSONObject(ETC);
            updateItemValues(curItem, coinObject);
        }

        // XRP
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.XRP, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(XRP)) {
            JSONObject coinObject = dataObject.optJSONObject(XRP);
            updateItemValues(curItem, coinObject);
        }

        if (dataObject.has(DATE)) {
            long timestamp = dataObject.optLong(DATE);
            Log.d(TAG, "onPostExecute(). timestamp: " + timestamp + ", curTime: " + System.currentTimeMillis());
        }
        adapterInstance.notifyDataSetChanged();
    }

    private void updateItemValues(ListViewItem item, JSONObject coinObject) {
        try {
            int openPrice = coinObject.getInt(OPENING_PRICE);
            int highPrice = coinObject.getInt(MAX_PRICE);
            int lowPrice = coinObject.getInt(MIN_PRICE);
            int curPrice = coinObject.getInt(CLOSING_PRICE);
            double volume = coinObject.getDouble(VOLUME_1DAY);

            item.setPrice(openPrice, highPrice, lowPrice, curPrice, volume);
            Log.d(TAG, item.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
