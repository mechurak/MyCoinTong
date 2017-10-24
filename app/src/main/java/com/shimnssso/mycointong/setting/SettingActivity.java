package com.shimnssso.mycointong.setting;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shimnssso.mycointong.Const;
import com.shimnssso.mycointong.R;
import com.shimnssso.mycointong.data.CoinInfo;
import com.shimnssso.mycointong.data.DbHelper;
import com.shimnssso.mycointong.data.DbMeta;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, SettingAdapter.SelectListener {
    private static final String TAG = "SettingActivity";
    SettingAdapter adapter;
    LinearLayout layout_updown;
    Button btn_delete;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "LifeCycle. onCreate()");

        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new SettingAdapter(this);
        DbHelper dbHelper = DbHelper.getInstance(this);
        ArrayList<CoinInfo> coinList = dbHelper.getInterestingCoinList(1);
        for (CoinInfo coinRow : coinList) {
            String coinFullName = coinRow.coin + "/" + coinRow.currency + "(" + coinRow.exchange + ")";
            adapter.addItem(new CoinItem(coinRow.coinId, coinFullName));
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        Button btn_up = (Button) findViewById(R.id.btn_up);
        btn_up.setOnClickListener(this);
        Button btn_down = (Button) findViewById(R.id.btn_down);
        btn_down.setOnClickListener(this);
        layout_updown = (LinearLayout) findViewById(R.id.layout_updown);

        btn_add.setVisibility(View.VISIBLE);
        btn_delete.setVisibility(View.GONE);
        layout_updown.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_delete:
                Log.d(TAG, "DELETE button was clicked");
                if (adapter != null) {
                    adapter.deleteSelected();
                }
                break;
            case R.id.btn_add:
                Log.d(TAG, "ADD button was clicked");
                if (adapter != null) {
                    Intent intent = new Intent(this, AddActivity.class);
                    intent.putExtra(AddActivity.INTENT_KEY_COIN_LIST, adapter.getCoinIdList());
                    startActivityForResult(intent, Const.RequestCode.AddActivity);
                } else {
                    Log.e(TAG, "adapter is null");
                }
                break;
            case R.id.btn_up:
                Log.d(TAG, "UP button was clicked");
                if (adapter != null) {
                    adapter.handleUpDown(SettingAdapter.BTN_TYPE_UP);
                }
                break;
            case R.id.btn_down:
                Log.d(TAG, "DOWN button was clicked");
                if (adapter != null) {
                    adapter.handleUpDown(SettingAdapter.BTN_TYPE_DOWN);
                }
                break;
            default:
                Log.w(TAG, "unexpected button. id: " + id);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult(). requestCode: " + requestCode + ", resultCode: " + resultCode);
        switch (requestCode) {
            case Const.RequestCode.AddActivity:
                if (resultCode == RESULT_OK) {
                    ArrayList<Integer> newCoinIdList = data.getIntegerArrayListExtra(AddActivity.INTENT_KEY_COIN_LIST);
                    Log.d(TAG, "newCoinIdList: " + newCoinIdList);
                    if (adapter != null) {
                        for (Integer coinId : newCoinIdList) {
                            String coinFullName = DbHelper.getInstance(this).getFullNameFromId(coinId);
                            adapter.addItem(new CoinItem(coinId, coinFullName));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "adapter is null.");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_confirm:
                Log.d(TAG, "CONFIRM button was clicked");
                if (adapter != null) {
                    ArrayList<Integer> newCoinList = adapter.getCoinIdList();
                    DbHelper dbHelper = DbHelper.getInstance(this);

                    // reset order
                    dbHelper.resetOrder(1);

                    // re-order
                    int curOrder = 1;
                    for (int coinId : newCoinList) {
                        dbHelper.upsertInterestRow(1, coinId, curOrder++);
                    }

                    // remove deleted items
                    dbHelper.removeDeletedRow(1, curOrder);

                    setResult(RESULT_OK);
                    this.finish();
                } else {
                    Log.e(TAG, "adapter is null");
                }
                return true;
            case R.id.action_cancel:
                Log.d(TAG, "CANCEL button was clicked");
                setResult(RESULT_CANCELED);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSelectCountChanged(int selectCount) {
        if (selectCount == 0) {
            btn_add.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.GONE);
            layout_updown.setVisibility(View.GONE);
        } else if (selectCount == 1) {
            btn_add.setVisibility(View.GONE);
            btn_delete.setVisibility(View.VISIBLE);
            layout_updown.setVisibility(View.VISIBLE);
        } else if (selectCount > 1) {
            btn_add.setVisibility(View.GONE);
            btn_delete.setVisibility(View.VISIBLE);
            layout_updown.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "unexpected selectCount. " + selectCount);
        }
    }
}
