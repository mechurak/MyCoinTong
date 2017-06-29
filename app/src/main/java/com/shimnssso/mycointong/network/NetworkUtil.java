package com.shimnssso.mycointong.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    static String request(String _url){
        HttpURLConnection urlConn = null;

        try{
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Accept-Charset", "UTF-8");
            urlConn.setRequestProperty("Context_Type", "application/json");

            int responseCode = urlConn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "responsCode: " + responseCode + ", url: " + urlConn.getURL());
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            String line;
            String page = "";

            while ((line = reader.readLine()) != null){
                page += line;
            }

            return page;

        } catch (IOException e) { // for URL. // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return null;
    }
}
