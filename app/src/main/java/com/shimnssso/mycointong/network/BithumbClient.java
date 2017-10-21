package com.shimnssso.mycointong.network;

import android.os.AsyncTask;
import android.util.Log;

import com.shimnssso.mycointong.Const;
import com.shimnssso.mycointong.ListViewAdapter;
import com.shimnssso.mycointong.ListViewItem;

import org.json.JSONException;
import org.json.JSONObject;

public class BithumbClient extends AsyncTask<Void, Void, String> {
    private static final String TAG = "BithumbClient";
    private static final String TICKER_URL = "https://api.bithumb.com/public/ticker/all";
    private static final String MY_CURRENCY = Const.Currency.KRW;
    private static final String MY_EXCHANGE = Const.Exchange.BITHUMB;

    // {currency} BTC, ETH, DASH, LTC, ETC, XRP, BCH, XMR, ZEC, QTUM (default: BTC), ALL
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
    private static String BCH = "BCH";
    private static String XMR = "XMR";
    private static String ZEC = "ZEC";
    private static String QTUM = "QTUM";

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

    private TickerListener mListener;
    public BithumbClient(TickerListener listener) {
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
            Log.e(TAG, "s == null");
            mListener.OnRefreshResult(Const.Exchange.BITHUMB, 0);
            return;
        }

        JSONObject responseObject;
        try {
            responseObject = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
            mListener.OnRefreshResult(Const.Exchange.BITHUMB, 0);
            return;
        }

        String status = null;
        if (responseObject.has(STATUS)) {
            status = responseObject.optString(STATUS, "FAIL");
        }
        if (status == null || !status.equals(STATUS_SUCCESS)) {
            Log.e(TAG, "status: " + status);
            mListener.OnRefreshResult(Const.Exchange.BITHUMB, 0);
            return;
        }

        JSONObject dataObject = null;
        if (responseObject.has(DATA)) {
            dataObject = responseObject.optJSONObject(DATA);
        }
        if (dataObject == null) {
            Log.e(TAG, "dataObject == null");
            mListener.OnRefreshResult(Const.Exchange.BITHUMB, 0);
            return;
        }

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;

        // BTC
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BTC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(BTC)) {
            JSONObject coinObject = dataObject.optJSONObject(BTC);
            updateItemValues(curItem, coinObject);
        }

        // BCH
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BCH, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(BCH)) {
            JSONObject coinObject = dataObject.optJSONObject(BCH);
            updateItemValues(curItem, coinObject);
        }

        // ETH
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.ETH, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(ETH)) {
            JSONObject coinObject = dataObject.optJSONObject(ETH);
            updateItemValues(curItem, coinObject);
        }

        // DASH
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.DASH, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(DASH)) {
            JSONObject coinObject = dataObject.optJSONObject(DASH);
            updateItemValues(curItem, coinObject);
        }

        // LTC
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.LTC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(LTC)) {
            JSONObject coinObject = dataObject.optJSONObject(LTC);
            updateItemValues(curItem, coinObject);
        }

        // ETC
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.ETC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(ETC)) {
            JSONObject coinObject = dataObject.optJSONObject(ETC);
            updateItemValues(curItem, coinObject);
        }

        // XRP
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.XRP, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(XRP)) {
            JSONObject coinObject = dataObject.optJSONObject(XRP);
            updateItemValues(curItem, coinObject);
        }

        // XMR
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.XMR, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(XMR)) {
            JSONObject coinObject = dataObject.optJSONObject(XMR);
            updateItemValues(curItem, coinObject);
        }

        // ZEC
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.ZEC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(ZEC)) {
            JSONObject coinObject = dataObject.optJSONObject(ZEC);
            updateItemValues(curItem, coinObject);
        }

        // QTUM
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.QTUM, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && dataObject.has(QTUM)) {
            JSONObject coinObject = dataObject.optJSONObject(QTUM);
            updateItemValues(curItem, coinObject);
        }

        if (dataObject.has(DATE)) {
            long timestamp = dataObject.optLong(DATE);
            Log.d(TAG, "onPostExecute(). timestamp: " + timestamp + ", curTime: " + System.currentTimeMillis());
        }

        adapterInstance.notifyDataSetChanged();
        mListener.OnRefreshResult(Const.Exchange.BITHUMB, 1);
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
