package com.shimnssso.mycointong;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shimnssso.mycointong.network.BithumClient;
import com.shimnssso.mycointong.network.CoinoneClient;
import com.shimnssso.mycointong.network.KorbitClient;

public class KoreaListFragment extends ListFragment {
    private static final String TAG = "KoreaListFragment";

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // get TextView's Text.
        ListViewItem item = (ListViewItem ) l.getItemAtPosition(position) ;
        Log.d(TAG, "onListItemClick(). position: " + position + ", id: " + id + "name: " + item.getName());

        String chartSite = item.getCoinoneChartSite();
        if (!chartSite.equals(Constant.ChartSite.NOT_SUPPORT)) {
            String url = String.format("https://coinone.co.kr/chart/?site=%s&unit_time=%s", chartSite, item.getUnitTime());
            Log.d(TAG, "onListItemClick(). url: " + url);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        else {
            Snackbar.make(v, "Coninone ProChart does not support " + item.getName(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "LifeCycle. onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "LifeCycle. onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "LifeCycle. onCreateView()");

        BithumClient bithumClient = new BithumClient();
        bithumClient.execute();
        CoinoneClient coinoneClient = new CoinoneClient();
        coinoneClient.execute();
        KorbitClient korbitClient = new KorbitClient();
        korbitClient.execute();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "LifeCycle. onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "LifeCycle. onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "LifeCycle. onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "LifeCycle. onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "LifeCycle. onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "LifeCycle. onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "LifeCycle. onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "LifeCycle. onDetach()");
    }
}
