package com.shimnssso.mycointong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shimnssso.mycointong.widget.CurrencyEditText;

public class HoldingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = HoldingActivity.class.getSimpleName();

    TextView mTextCoinName;
    Button mBtnConfirm, mBtnCancel;
    String mCoin;
    String mCurrency;
    String mExchange;
    Double mAvgPrice, mQuantity;
    CurrencyEditText mEditAvgPrice;
    EditText mEditQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mCoin = intent.getStringExtra(Const.HoldingIntentKey.Coin);
        mCurrency = intent.getStringExtra(Const.HoldingIntentKey.Currency);
        mExchange = intent.getStringExtra(Const.HoldingIntentKey.Exchange);
        mAvgPrice = intent.getDoubleExtra(Const.HoldingIntentKey.AvgPrice, 0.0d);
        mQuantity = intent.getDoubleExtra(Const.HoldingIntentKey.Quantity, 0.0d);

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

        mEditAvgPrice = (CurrencyEditText) findViewById(R.id.editAvgPrice);
        mEditAvgPrice.setHint(mAvgPrice.toString());
        mEditQuantity = (EditText) findViewById(R.id.editQuantity);
        mEditQuantity.setHint(mQuantity.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                try {
                    double avgPrice = mAvgPrice;
                    String inputAvgPrice = mEditAvgPrice.getText().toString();
                    if (inputAvgPrice.length() > 0) {
                        inputAvgPrice = inputAvgPrice.replaceAll("[,]", "");
                        avgPrice = Double.parseDouble(inputAvgPrice);
                    }
                    double quantity = mQuantity;
                    String inputQuantity = mEditQuantity.getText().toString();
                    if (inputQuantity.length() > 0) {
                        inputQuantity = inputQuantity.replaceAll("[,]", "");
                        quantity = Double.parseDouble(inputQuantity);
                    }
                    Intent intent = new Intent();
                    intent.putExtra(Const.HoldingIntentKey.Coin, mCoin);
                    intent.putExtra(Const.HoldingIntentKey.Currency, mCurrency);
                    intent.putExtra(Const.HoldingIntentKey.Exchange, mExchange);
                    intent.putExtra(Const.HoldingIntentKey.AvgPrice, avgPrice);
                    intent.putExtra(Const.HoldingIntentKey.Quantity, quantity);
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
