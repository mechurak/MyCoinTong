package com.shimnssso.mycointong.exchangerate;

import android.util.Log;

public class FinanceHelper {
    public static final String TAG = "FinanceHelper";

    private static float mUsdKrw = 1078.31f;
    private static float mUsdJpy = 113.378f;
    private static long mUpdateTime = 1513868186253L;

    public static float getUsdKrw() {
        return mUsdKrw;
    }

    public static void setUsdKrw(float usdKrw) {
        mUsdKrw = usdKrw;
        Log.i(TAG, "setUsdKrw. " + usdKrw);
    }

    public static float getUsdJpy() {
        return mUsdJpy;
    }

    public static void setUsdJpy(float usdJpy) {
        mUsdJpy = usdJpy;
        Log.i(TAG, "setUsdJpy. " + usdJpy);
    }

    public static long getUpdateTime() {
        return mUpdateTime;
    }
    public static void setUpdateTime(long updateTime) {
        mUpdateTime = updateTime;
    }
}
