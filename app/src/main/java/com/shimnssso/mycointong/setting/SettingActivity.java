package com.shimnssso.mycointong.setting;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.shimnssso.mycointong.Constant;
import com.shimnssso.mycointong.R;
import com.shimnssso.mycointong.data.CoinInfo;
import com.shimnssso.mycointong.data.DbHelper;
import com.shimnssso.mycointong.data.DbMeta;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettingActivity";
    SettingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "LifeCycle. onCreate()");

        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new SettingAdapter();
        DbHelper dbHelper = DbHelper.getInstance(this);
        ArrayList<CoinInfo> coinList = dbHelper.getInterestingCoinList();
        for (CoinInfo coinRow : coinList) {
            String coinFullName = coinRow.coin + "/" + coinRow.currency + "(" + coinRow.exchange + ")";
            adapter.addItem(new CoinItem(coinFullName));
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_delete:
                Log.d(TAG, "DELETE button was clicked");
                if (adapter != null) {
                    int deleteCount = adapter.deleteSelected();
                    if (deleteCount == 1) {
                        Toast.makeText(getApplicationContext(), deleteCount + " item is deleted.", Toast.LENGTH_SHORT).show();
                    } else if (deleteCount > 0) {
                        Toast.makeText(getApplicationContext(), deleteCount + " items are deleted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "There are no items selected.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_add:
                Log.d(TAG, "ADD button was clicked");
                if (adapter != null) {
                    Intent intent = new Intent(this, AddActivity.class);
                    intent.putExtra(AddActivity.INTENT_KEY_COIN_LIST, adapter.getCoinFullNameList());
                    startActivityForResult(intent, Constant.RequestCode.AddActivity);
                } else {
                    Log.e(TAG, "adapter is null");
                }
                break;
            case R.id.btn_cancel:
                Log.d(TAG, "CANCEL button was clicked");
                setResult(RESULT_CANCELED);
                this.finish();
                break;
            case R.id.btn_confirm:
                Log.d(TAG, "CONFIRM button was clicked");
                if (adapter != null) {
                    ArrayList<String> newCoinList = adapter.getCoinFullNameList();
                    DbHelper dbHelper = DbHelper.getInstance(this);

                    // set interest 0 for all coins
                    ContentValues contentForRefresh = new ContentValues();
                    contentForRefresh.put(DbMeta.CoinTableMeta.INTEREST, 0);
                    dbHelper.updateCoinTable(contentForRefresh, null);

                    // set interest and order for specific coins
                    int order = 0;
                    for (String coinFullName : newCoinList) {
                        ContentValues content = new ContentValues();
                        content.put(DbMeta.CoinTableMeta.INTEREST, 1);
                        content.put(DbMeta.CoinTableMeta.LIST_ORDER, order++);
                        dbHelper.updateCoinTable(content, coinFullName);
                    }

                    setResult(RESULT_OK);
                    this.finish();
                } else {
                    Log.e(TAG, "adapter is null");
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
            case Constant.RequestCode.AddActivity:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> newCoinList = data.getStringArrayListExtra(AddActivity.INTENT_KEY_COIN_LIST);
                    Log.d(TAG, "newCoinList: " + newCoinList);
                    if (adapter != null) {
                        for (String coinFullName : newCoinList) {
                            adapter.addItem(new CoinItem(coinFullName));
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
}
