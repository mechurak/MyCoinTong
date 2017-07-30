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
    private static final String MY_CURRENCY = Constant.Currency.KRW;
    private static final String MY_EXCHANGE = Constant.Exchange.KORBIT;

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

    private TickerListener mListener;
    public KorbitClient(TickerListener listener) {
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
            mListener.OnRefreshResult(Constant.Exchange.KORBIT, 0);
            return;
        }

        JSONObject responseObject;
        try {
            responseObject = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
            mListener.OnRefreshResult(Constant.Exchange.KORBIT, 0);
            return;
        }

        ListViewAdapter adapterInstance = ListViewAdapter.getInstance();
        ListViewItem curItem;

        // BTC
        curItem = (ListViewItem) adapterInstance.getItemByName(Constant.Coin.BTC, MY_CURRENCY, MY_EXCHANGE);
        if (curItem != null && responseObject.has(TIME_STAMP)) {
            updateItemValues(curItem, responseObject);
        }

        adapterInstance.notifyDataSetChanged();
        mListener.OnRefreshResult(Constant.Exchange.KORBIT, 1);
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
