package com.shimnssso.mycointong.network;

import android.os.AsyncTask;
import android.util.Log;

import com.shimnssso.mycointong.Const;
import com.shimnssso.mycointong.ListViewAdapter;
import com.shimnssso.mycointong.ListViewItem;

import org.json.JSONException;
import org.json.JSONObject;

public class KorbitClient extends AsyncTask<Void, Void, JSONObject> {
    private static final String TAG = "KorbitClient";
    private static final String TICKER_URL = "https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=";
    private static final String MY_CURRENCY = Const.Currency.KRW;
    private static final String MY_EXCHANGE = Const.Exchange.KORBIT;

    // parameter
    // currency_pair	btc_krw (default), etc_krw, eth_krw, xrp_krw
    private static String BTC_KRW = "btc_krw";
    private static String BCH_KRW = "bch_krw";
    private static String ETC_KRW = "etc_krw";
    private static String ETH_KRW = "eth_krw";
    private static String XRP_KRW = "xrp_krw";

    // response
    private static String TIME_STAMP = "timestamp";
    private static String LAST = "last";
    private static String BID = "bid";
    private static String ASK = "ask";
    private static String LOW = "low";
    private static String HIGH = "high";
    private static String VOLUME = "volume";
    private static String CHANGE = "change";

    private TickerListener mListener;
    public KorbitClient(TickerListener listener) {
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
            // BTC
            curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BTC, MY_CURRENCY, MY_EXCHANGE);
            if (curItem != null) {
                retString = NetworkUtil.request(TICKER_URL + BTC_KRW);
                if (retString == null) return null;
                curRetJson = new JSONObject(retString);
                mergedRetJson.put(BTC_KRW, curRetJson);
            }

            // BCH
            curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BCH, MY_CURRENCY, MY_EXCHANGE);
            if (curItem != null) {
                retString = NetworkUtil.request(TICKER_URL + BCH_KRW);
                if (retString == null) return null;
                curRetJson = new JSONObject(retString);
                mergedRetJson.put(BCH_KRW, curRetJson);
            }

            // ETC
            curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.ETC, MY_CURRENCY, MY_EXCHANGE);
            if (curItem != null) {
                retString = NetworkUtil.request(TICKER_URL + ETC_KRW);
                if (retString == null) return null;
                curRetJson = new JSONObject(retString);
                mergedRetJson.put(ETC_KRW, curRetJson);
            }

            // ETH
            curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.ETH, MY_CURRENCY, MY_EXCHANGE);
            if (curItem != null) {
                retString = NetworkUtil.request(TICKER_URL + ETH_KRW);
                if (retString == null) return null;
                curRetJson = new JSONObject(retString);
                mergedRetJson.put(ETH_KRW, curRetJson);
            }

            // XRP
            curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.XRP, MY_CURRENCY, MY_EXCHANGE);
            if (curItem != null) {
                retString = NetworkUtil.request(TICKER_URL + XRP_KRW);
                if (retString == null) return null;
                curRetJson = new JSONObject(retString);
                mergedRetJson.put(XRP_KRW, curRetJson);
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
            Log.e(TAG, "s == null");
            mListener.OnRefreshResult(Const.Exchange.KORBIT, 0);
            return;
        }

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;

        // BTC
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BTC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && mergedRetJson.has(BTC_KRW)) {
            JSONObject coinObject = mergedRetJson.optJSONObject(BTC_KRW);
            updateItemValues(curItem, coinObject);
        }

        // BCH
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.BCH, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && mergedRetJson.has(BCH_KRW)) {
            JSONObject coinObject = mergedRetJson.optJSONObject(BCH_KRW);
            updateItemValues(curItem, coinObject);
        }

        // ETC
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.ETC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && mergedRetJson.has(ETC_KRW)) {
            JSONObject coinObject = mergedRetJson.optJSONObject(ETC_KRW);
            updateItemValues(curItem, coinObject);
        }

        // ETH
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.ETH, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && mergedRetJson.has(ETH_KRW)) {
            JSONObject coinObject = mergedRetJson.optJSONObject(ETH_KRW);
            updateItemValues(curItem, coinObject);
        }

        // XRP
        curItem = (ListViewItem) adapterInstance.getItemByName(Const.Coin.XRP, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && mergedRetJson.has(XRP_KRW)) {
            JSONObject coinObject = mergedRetJson.optJSONObject(XRP_KRW);
            updateItemValues(curItem, coinObject);
        }

        adapterInstance.notifyDataSetChanged();
        mListener.OnRefreshResult(Const.Exchange.KORBIT, 1);
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
