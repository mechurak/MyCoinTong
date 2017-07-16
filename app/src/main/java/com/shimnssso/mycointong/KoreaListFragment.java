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
import android.widget.AdapterView;
import android.widget.ListView;

import com.shimnssso.mycointong.data.DbHelper;
import com.shimnssso.mycointong.network.BithumClient;
import com.shimnssso.mycointong.network.CoinoneClient;
import com.shimnssso.mycointong.network.KorbitClient;

import static android.app.Activity.RESULT_OK;

public class KoreaListFragment extends ListFragment implements AdapterView.OnItemLongClickListener {
    private static final String TAG = "KoreaListFragment";

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // get TextView's Text.
        ListViewItem item = (ListViewItem) l.getItemAtPosition(position) ;
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListViewItem item = (ListViewItem ) parent.getItemAtPosition(position) ;
        Log.d(TAG, "onItemLongClick(). position: " + position + ", id: " + id + "name: " + item.getName());
        Intent intent = new Intent(getActivity(), HoldingActivity.class);
        intent.putExtra(Constant.HoldingIntentKey.CoinName, item.getName());
        startActivityForResult(intent, Constant.RequestCode.HoldingActivity);
        return true;
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
        refresh();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "LifeCycle. onActivityCreated()");

        getListView().setOnItemLongClickListener(this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult(). requestCode: " + requestCode + ", resultCode: " + resultCode);
        switch (requestCode) {
            case Constant.RequestCode.HoldingActivity:
                if (resultCode == RESULT_OK) {
                    String coinName = data.getStringExtra(Constant.HoldingIntentKey.CoinName);
                    double avgPrice = data.getDoubleExtra(Constant.HoldingIntentKey.AvgPrice, 0.0d);
                    double quantity = data.getDoubleExtra(Constant.HoldingIntentKey.Quantity, 0.0d);
                    Log.d(TAG, "coin: " + coinName + ", avgPrice: " + avgPrice + ", quantity: " + quantity);

                    DbHelper dbHelper = DbHelper.getInstance(getContext());
                    dbHelper.updateHolding(coinName, avgPrice, quantity);

                    ListViewAdapter adapter = ListViewAdapter.getInstance();
                    ListViewItem item = (ListViewItem) adapter.getItemByName(coinName);
                    item.setMyAvgPrice(avgPrice);
                    item.setMyQuantity(quantity);

                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    public static void refresh() {
        BithumClient bithumClient = new BithumClient();
        bithumClient.execute();
        CoinoneClient coinoneClient = new CoinoneClient();
        coinoneClient.execute();
        KorbitClient korbitClient = new KorbitClient();
        korbitClient.execute();
    }
}
