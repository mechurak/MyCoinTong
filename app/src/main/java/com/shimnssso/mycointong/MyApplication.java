package com.shimnssso.mycointong;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();

        // (url) chrome://inspect
        Stetho.initializeWithDefaults(this);
    }
}
