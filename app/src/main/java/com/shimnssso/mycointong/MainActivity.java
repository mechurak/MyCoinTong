package com.shimnssso.mycointong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shimnssso.mycointong.data.CoinInfo;
import com.shimnssso.mycointong.data.DbHelper;
import com.shimnssso.mycointong.network.BithumClient;
import com.shimnssso.mycointong.network.CoinoneClient;
import com.shimnssso.mycointong.network.KorbitClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, ListView.OnItemLongClickListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "LifeCycle. onCreate()");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListViewAdapter adapter = ListViewAdapter.getInstance();
        if (adapter.getCount() == 0) {
            DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
            Log.e(TAG, "after db");
            ArrayList<CoinInfo> coinList = dbHelper.getInterestingCoinList();
            for (CoinInfo coinRow : coinList) {
                Log.i(TAG, coinRow.toString());
                ListViewItem item = new ListViewItem(coinRow.coin, coinRow.currency, coinRow.exchange, coinRow.chartCoinone);
                item.setMyAvgPrice(coinRow.avgPrice);
                item.setMyQuantity(coinRow.quantity);
                adapter.addItem(item);
            }
            ListViewItem tempItem = new ListViewItem("TEST");
            adapter.addItem(tempItem);
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "LifeCycle. onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "LifeCycle. onStart()");
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "LifeCycle. onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "LifeCycle. onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "LifeCycle. onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "LifeCycle. onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult(). requestCode: " + requestCode + ", resultCode: " + resultCode);
        switch (requestCode) {
            case Constant.RequestCode.HoldingActivity:
                if (resultCode == RESULT_OK) {
                    String coin = data.getStringExtra(Constant.HoldingIntentKey.Coin);
                    String currency = data.getStringExtra(Constant.HoldingIntentKey.Currency);
                    String exchange = data.getStringExtra(Constant.HoldingIntentKey.Exchange);
                    double avgPrice = data.getDoubleExtra(Constant.HoldingIntentKey.AvgPrice, 0.0d);
                    double quantity = data.getDoubleExtra(Constant.HoldingIntentKey.Quantity, 0.0d);
                    Log.d(TAG, "coin: " + coin + ", avgPrice: " + avgPrice + ", quantity: " + quantity);

                    DbHelper dbHelper = DbHelper.getInstance(this);
                    dbHelper.updateHolding(coin, currency, exchange, avgPrice, quantity);

                    ListViewAdapter adapter = ListViewAdapter.getInstance();
                    ListViewItem item = (ListViewItem) adapter.getItemByName(coin, currency, exchange);
                    item.setMyAvgPrice(avgPrice);
                    item.setMyQuantity(quantity);

                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // get TextView's Text.
        ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;
        Log.d(TAG, "onListItemClick(). position: " + position + ", id: " + id + "name: " + item.getName());

        String chartSite = item.getCoinoneChartSite();
        if (!chartSite.equals(Constant.ChartSite.NOT_SUPPORT)) {
            String url = String.format("https://coinone.co.kr/chart/?site=%s&unit_time=%s", chartSite, item.getUnitTime());
            Log.d(TAG, "onListItemClick(). url: " + url);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        else {
            Snackbar.make(parent, "Coninone ProChart does not support " + item.getFullName(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListViewItem item = (ListViewItem ) parent.getItemAtPosition(position) ;
        Log.d(TAG, "onItemLongClick(). position: " + position + ", id: " + id + "name: " + item.getFullName());
        Intent intent = new Intent(this, HoldingActivity.class);
        intent.putExtra(Constant.HoldingIntentKey.Coin, item.getCoin());
        intent.putExtra(Constant.HoldingIntentKey.Currency, item.getCurrency());
        intent.putExtra(Constant.HoldingIntentKey.Exchange, item.getExchange());
        intent.putExtra(Constant.HoldingIntentKey.AvgPrice, item.getMyAvgPrice());
        intent.putExtra(Constant.HoldingIntentKey.Quantity, item.getMyQuantity());
        startActivityForResult(intent, Constant.RequestCode.HoldingActivity);
        return true;
    }

    private void refresh() {
        Log.d(TAG, "refresh()");
        BithumClient bithumClient = new BithumClient();
        bithumClient.execute();
        CoinoneClient coinoneClient = new CoinoneClient();
        coinoneClient.execute();
        KorbitClient korbitClient = new KorbitClient();
        korbitClient.execute();
    }
}
