package com.shimnssso.mycointong;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "LifeCycle. onCreate()");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init listViewAdapter
        ListViewAdapter adapter = ListViewAdapter.getInstance();
        ListViewItem korbitItem = new ListViewItem(Constant.CoinName.BTC_KORBIT);
        korbitItem.setCoinoneChartSite(Constant.ChartSite.BTC_KORBIT);
        adapter.addItem(korbitItem);

        ListViewItem bithumItem = new ListViewItem(Constant.CoinName.BTC_BITHUM);
        bithumItem.setCoinoneChartSite(Constant.ChartSite.BTC_BITHUM);
        adapter.addItem(bithumItem);

        ListViewItem coinoneItem = new ListViewItem(Constant.CoinName.BTC_COINONE);
        coinoneItem.setCoinoneChartSite(Constant.ChartSite.BTC_COINONE);
        adapter.addItem(coinoneItem);

        ListViewItem ethBithumItem = new ListViewItem(Constant.CoinName.ETH_BITHUM);
        adapter.addItem(ethBithumItem);

        ListViewItem ethCoinoneItem = new ListViewItem(Constant.CoinName.ETH_COINONE);
        ethCoinoneItem.setCoinoneChartSite(Constant.ChartSite.ETH_COINONE);
        adapter.addItem(ethCoinoneItem);

        ListViewItem xrpBithumItem = new ListViewItem(Constant.CoinName.XRP_BITHUM);
        adapter.addItem(xrpBithumItem);

        ListViewItem xrpCoinoneItem = new ListViewItem(Constant.CoinName.XRP_COINONE);
        xrpCoinoneItem.setCoinoneChartSite(Constant.ChartSite.XRP_COINONE);
        adapter.addItem(xrpCoinoneItem);

        ListViewItem tempItem = new ListViewItem("TEST");
        adapter.addItem(tempItem);

        // Init fragment
        ListFragment koreaListFrgmt = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.korea_list_fragment);
        koreaListFrgmt.setListAdapter(adapter);
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
}
