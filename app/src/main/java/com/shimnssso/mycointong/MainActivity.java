package com.shimnssso.mycointong;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.shimnssso.mycointong.data.DbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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
            ArrayList<String[]> coinList = dbHelper.getInterestingCoinList();
            for (String[] coinRow : coinList) {
                Log.e(TAG, coinRow[0] + ", " + coinRow[1]);
                ListViewItem item = new ListViewItem(coinRow[0], coinRow[1]);
                adapter.addItem(item);
            }
            ListViewItem tempItem = new ListViewItem("TEST");
            adapter.addItem(tempItem);
        }

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
