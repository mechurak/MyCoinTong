package com.shimnssso.mycointong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.shimnssso.mycointong.network.BitfinexClient;
import com.shimnssso.mycointong.network.BithumClient;
import com.shimnssso.mycointong.network.CoinoneClient;
import com.shimnssso.mycointong.network.CryptowatchClient;
import com.shimnssso.mycointong.network.KorbitClient;
import com.shimnssso.mycointong.network.TickerListener;
import com.shimnssso.mycointong.setting.SettingActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, ListView.OnItemLongClickListener, TickerListener {
    private static final String TAG = "MainActivity";

    private static final long REFRESH_INTERVAL_DEFAULT = 1000 * 60;  // 1 min

    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "LifeCycle. onCreate()");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListViewAdapter adapter = ListViewAdapter.getInstance();
        if (adapter.getCount() == 0) {
            DbHelper dbHelper = DbHelper.getInstance(this);
            Log.e(TAG, "after db");
            ArrayList<CoinInfo> coinList = dbHelper.getInterestingCoinList();
            for (CoinInfo coinRow : coinList) {
                Log.i(TAG, coinRow.toString());
                ListViewItem item = new ListViewItem(coinRow.coin, coinRow.currency, coinRow.exchange, coinRow.chartCoinone);
                item.setMyAvgPrice(coinRow.avgPrice);
                item.setMyQuantity(coinRow.quantity);
                adapter.addItem(item);
            }
            ListViewItem tempItem = new ListViewItem("TEST", Const.Currency.USD, Const.Exchange.BITFINEX, "");
            adapter.addItem(tempItem);
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
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
        DbHelper dbHelper = DbHelper.getInstance(this);
        long prevUpdateTime = dbHelper.getUpdateTime();
        if (System.currentTimeMillis() - prevUpdateTime >= REFRESH_INTERVAL_DEFAULT) {
            refresh();
        } else {
            Log.d(TAG, "skip refresh");
        }
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
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit_coin_list:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, Const.RequestCode.SettingActivity);
                return true;
            case R.id.action_refresh:
                mSwipeLayout.setRefreshing(true);  // explicit call is needed (non swipe gesture case)
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult(). requestCode: " + requestCode + ", resultCode: " + resultCode);
        switch (requestCode) {
            case Const.RequestCode.HoldingActivity:
                if (resultCode == RESULT_OK) {
                    String coin = data.getStringExtra(Const.HoldingIntentKey.Coin);
                    String currency = data.getStringExtra(Const.HoldingIntentKey.Currency);
                    String exchange = data.getStringExtra(Const.HoldingIntentKey.Exchange);
                    double avgPrice = data.getDoubleExtra(Const.HoldingIntentKey.AvgPrice, 0.0d);
                    double quantity = data.getDoubleExtra(Const.HoldingIntentKey.Quantity, 0.0d);
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
            case Const.RequestCode.SettingActivity:
                if (resultCode == RESULT_OK) {
                    updateAdapterFromDb();
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
        if (chartSite != null && chartSite.startsWith("http")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(chartSite));
            startActivity(intent);
        }
        else {
            Snackbar.make(parent, "Suitable chart site is not available for " + item.getFullName(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListViewItem item = (ListViewItem ) parent.getItemAtPosition(position) ;
        Log.d(TAG, "onItemLongClick(). position: " + position + ", id: " + id + "name: " + item.getFullName());
        Intent intent = new Intent(this, HoldingActivity.class);
        intent.putExtra(Const.HoldingIntentKey.Coin, item.getCoin());
        intent.putExtra(Const.HoldingIntentKey.Currency, item.getCurrency());
        intent.putExtra(Const.HoldingIntentKey.Exchange, item.getExchange());
        intent.putExtra(Const.HoldingIntentKey.AvgPrice, item.getMyAvgPrice());
        intent.putExtra(Const.HoldingIntentKey.Quantity, item.getMyQuantity());
        startActivityForResult(intent, Const.RequestCode.HoldingActivity);
        return true;
    }

    private static final Object mRefreshLock = new Object();
    private ArrayList<String> mRefreshList;
    @Override
    public void OnRefreshResult(String site, int result) {
        synchronized (mRefreshLock) {
            Log.d(TAG, "OnRefreshResult(). site: " + site + ", result: " + result);
            if (mRefreshList != null) {
                boolean removeRet = mRefreshList.remove(site);
                Log.d(TAG, "removeRet: " + removeRet);

                if (mRefreshList.isEmpty()) mSwipeLayout.setRefreshing(false);
            }
        }
    }

    private void refresh() {
        Log.d(TAG, "refresh()");
        synchronized (mRefreshLock) {
            mRefreshList = new ArrayList<>();
            BithumClient bithumClient = new BithumClient(this);
            bithumClient.execute();
            mRefreshList.add(Const.Exchange.BITHUMB);
            CoinoneClient coinoneClient = new CoinoneClient(this);
            coinoneClient.execute();
            mRefreshList.add(Const.Exchange.COINONE);
            KorbitClient korbitClient = new KorbitClient(this);
            korbitClient.execute();
            mRefreshList.add(Const.Exchange.KORBIT);
//            CryptowatchClient cryptowatchClient = new CryptowatchClient(this);
//            cryptowatchClient.execute();
//            mRefreshList.add(Const.Exchange.CRYPTOWATCH);
            BitfinexClient bitfinexClient = new BitfinexClient(this);
            bitfinexClient.execute();
            mRefreshList.add(Const.Exchange.BITFINEX);
        }

        DbHelper dbHelper = DbHelper.getInstance(this);
        dbHelper.setUpdateTime(System.currentTimeMillis());
    }

    private void updateAdapterFromDb() {
        Log.d(TAG, "updateAdapterFromDb()");

        ListViewAdapter adapter = ListViewAdapter.getInstance();
        adapter.removeAllItem();

        DbHelper dbHelper = DbHelper.getInstance(this);
        ArrayList<CoinInfo> coinList = dbHelper.getInterestingCoinList();
        for (CoinInfo coinRow : coinList) {
            Log.i(TAG, coinRow.toString());
            ListViewItem item = new ListViewItem(coinRow.coin, coinRow.currency, coinRow.exchange, coinRow.chartCoinone);
            item.setMyAvgPrice(coinRow.avgPrice);
            item.setMyQuantity(coinRow.quantity);
            adapter.addItem(item);
        }
        adapter.notifyDataSetChanged();
    }
}
