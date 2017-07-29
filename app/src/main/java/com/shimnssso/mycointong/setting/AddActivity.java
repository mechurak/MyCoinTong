package com.shimnssso.mycointong.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.shimnssso.mycointong.R;
import com.shimnssso.mycointong.data.DbHelper;

import java.util.ArrayList;

public class AddActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AddActivity";
    public static final String INTENT_KEY_COIN_LIST = "coin_list";

    ArrayList<String> mCurCoinList;
    AddAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();
        mCurCoinList = intent.getStringArrayListExtra(INTENT_KEY_COIN_LIST);
        Log.d(TAG, "mCurCoinList: " + mCurCoinList);

        setContentView(R.layout.activity_add);

        mAdapter = new AddAdapter();
        DbHelper dbHelper = DbHelper.getInstance(this);
        ArrayList<String> coinFullNameList = dbHelper.getAvailableCoinFullNameList();
        int candidateCount = 0;
        for (String coinFullName : coinFullNameList) {
            if (!mCurCoinList.contains(coinFullName)) {
                mAdapter.addItem(new CoinItem(coinFullName));
                candidateCount++;
            } else {
                Log.d(TAG, coinFullName + " is in the interesting coins.");
            }
        }
        if (candidateCount == 0) {
            Toast.makeText(getApplicationContext(), "All available coins are in the interesting coin list", Toast.LENGTH_SHORT).show();
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
            case R.id.btn_add:
                if (mAdapter != null) {
                    Intent intent = new Intent();
                    intent.putExtra(INTENT_KEY_COIN_LIST, mAdapter.getSeletedCoinFullNameList());
                    setResult(RESULT_OK, intent);
                    this.finish();
                } else {
                    Log.e(TAG, "mAdapter is null.");
                }
                break;
            default:
                break;
        }
    }
}
