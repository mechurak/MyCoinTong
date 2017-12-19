package com.shimnssso.mycointong.exchangerate;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shimnssso.mycointong.Const;
import com.shimnssso.mycointong.Util;
import com.shimnssso.mycointong.network.TickerListener;

public class UsdJpy {
    private static final String TAG = "UsdJpy";
    private static final String URL = "https://finance.yahoo.com/quote/usdjpy=X";

    private static final int MSG_CHECK_CURRENCY = 100;
    private static final int MSG_CHECK_TITLE = 101;

    private int counter = 0;
    private boolean alreadyFinished = false;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CHECK_CURRENCY:
                    mBrowser.loadUrl(URL);
                    break;
                case MSG_CHECK_TITLE:
                    String title = mBrowser.getTitle();
                    Log.d(TAG, "title: " + title);
                    float value = Util.getExchangeRateFromYahooTitle(title);
                    Log.d(TAG, "value: " + value);
                    if (value == 0.0f && counter < 5) {
                        mHandler.sendEmptyMessageDelayed(MSG_CHECK_TITLE, 2000);
                        counter++;
                    } else {
                        if (value != 0.0f) {
                            // update value
                            FinanceHelper.setUsdJpy(value);
                            mListener.OnRefreshResult(Const.ExchangeRate.USDJPY, 1);
                        } else {
                            mListener.OnRefreshResult(Const.ExchangeRate.USDJPY, 0);
                        }
                        mBrowser.removeAllViews();
                        mBrowser.destroy();
                    }
                    break;
            }
        }
    };

    private WebView mBrowser;

    private TickerListener mListener;
    public UsdJpy(Context context, TickerListener tickerListener) {
        mListener = tickerListener;

        mBrowser = new WebView(context);
        mBrowser.setVisibility(View.INVISIBLE);
        mBrowser.setLayerType(View.LAYER_TYPE_NONE, null);
        mBrowser.getSettings().setJavaScriptEnabled(true);
        mBrowser.getSettings().setBlockNetworkImage(true);
        mBrowser.getSettings().setDomStorageEnabled(false);
        mBrowser.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mBrowser.getSettings().setLoadsImagesAutomatically(false);
        mBrowser.getSettings().setGeolocationEnabled(false);
        mBrowser.getSettings().setSupportZoom(false);
        mBrowser.setWebViewClient(
            new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Log.d(TAG, "onPageStarted");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    Log.d(TAG, "onPageFinished");
                    if (!alreadyFinished) {
                        mHandler.sendEmptyMessageDelayed(MSG_CHECK_TITLE, 2000);
                        alreadyFinished = true;
                    }
                }
            }
        );
    }

    public void check() {
        mHandler.sendEmptyMessageDelayed(MSG_CHECK_CURRENCY, 200);
    }
}
