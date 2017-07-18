package com.shimnssso.mycointong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HoldingActivity extends Activity implements View.OnClickListener {
    private static final String TAG = HoldingActivity.class.getSimpleName();

    TextView mTextCoinName;
    Button mBtnConfirm, mBtnCancel;
    String mCoin;
    String mCurrency;
    String mExchange;
    EditText mEditAvgPrice, mEditQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();
        mCoin = intent.getStringExtra(Constant.HoldingIntentKey.Coin);
        mCurrency = intent.getStringExtra(Constant.HoldingIntentKey.Currency);
        mExchange = intent.getStringExtra(Constant.HoldingIntentKey.Exchange);

        setContentView(R.layout.activity_holding);
        setContent();
    }

    private void setContent() {
        mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mTextCoinName = (TextView) findViewById(R.id.textCoinName);
        mTextCoinName.setText(mCoin + "/" + mCurrency + "(" + mExchange + ")");

        mEditAvgPrice = (EditText) findViewById(R.id.editAvgPrice);
        mEditQuantity = (EditText) findViewById(R.id.editQuantity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                try {
                    double avgPrice = Double.parseDouble(mEditAvgPrice.getText().toString());
                    double quantity = Double.parseDouble(mEditQuantity.getText().toString());

                    Intent intent = new Intent();
                    intent.putExtra(Constant.HoldingIntentKey.Coin, mCoin);
                    intent.putExtra(Constant.HoldingIntentKey.Currency, mCurrency);
                    intent.putExtra(Constant.HoldingIntentKey.Exchange, mExchange);
                    intent.putExtra(Constant.HoldingIntentKey.AvgPrice, avgPrice);
                    intent.putExtra(Constant.HoldingIntentKey.Quantity, quantity);
                    setResult(RESULT_OK, intent);
                    this.finish();
                } catch (NumberFormatException | NullPointerException e) {
                    Log.w(TAG, "Failed to get avgPrice and quantity");
                    Toast.makeText(getApplicationContext(), "Please insert your average price and quantity.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
            default:
                break;
        }
    }
}
