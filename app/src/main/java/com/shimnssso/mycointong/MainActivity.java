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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.shimnssso.mycointong.data.CoinInfo;
import com.shimnssso.mycointong.data.DbHelper;
import com.shimnssso.mycointong.exchangerate.UsdJpy;
import com.shimnssso.mycointong.exchangerate.UsdKrw;
import com.shimnssso.mycointong.network.BitfinexClient;
import com.shimnssso.mycointong.network.BithumbClient;
import com.shimnssso.mycointong.network.CoinoneClient;
import com.shimnssso.mycointong.network.CryptowatchClient;
import com.shimnssso.mycointong.network.KorbitClient;
import com.shimnssso.mycointong.network.TickerListener;
import com.shimnssso.mycointong.setting.SettingActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, ListView.OnItemLongClickListener, TickerListener {
    private static final String TAG = "MainActivity";

    private static final long REFRESH_INTERVAL_DEFAULT = 1000 * 60;  // 1 min

    private SwipeRefreshLayout mSwipeLayout;
    private AdView mAdView;
    private View mFooter;
    DecimalFormat mIntFormatter = new DecimalFormat("#,###");
    DecimalFormat mFloatFormatter = new DecimalFormat("#,##0.00");

    private boolean isVeryFirstTime = false;

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
            ArrayList<CoinInfo> coinList = dbHelper.getInterestingCoinList(1);
            for (CoinInfo coinRow : coinList) {
                Log.i(TAG, coinRow.toString());
                ListViewItem item = new ListViewItem(coinRow.coinId, coinRow.coin, coinRow.currency, coinRow.exchange, coinRow.chartCoinone);
                item.setMyAvgPrice(coinRow.avgPrice);
                item.setMyQuantity(coinRow.quantity);
                adapter.addItem(item);
            }
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        mFooter = getLayoutInflater().inflate(R.layout.listview_footer, null, false);
        listView.addFooterView(mFooter);
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

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-5342329730936246~5303083484");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("AF19AD1C1F48D53DE32E2549DA15AE30")  // TODO: Remove it for release
                .addTestDevice("91E9EA5A56FC555CA14D4E263ACC5301")  // TODO: Remove it for release (S3)
                .build();
        mAdView.loadAd(adRequest);

        isVeryFirstTime = true;
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
        if (isVeryFirstTime) {
            refresh();
            isVeryFirstTime = false;
        }
        else {
            DbHelper dbHelper = DbHelper.getInstance(this);
            long prevUpdateTime = dbHelper.getUpdateTime();
            if (System.currentTimeMillis() - prevUpdateTime >= REFRESH_INTERVAL_DEFAULT) {
                refresh();
            } else {
                Log.d(TAG, "skip refresh");
            }
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
                    updateRevenue();
                }
                break;
            case Const.RequestCode.SettingActivity:
                if (resultCode == RESULT_OK) {
                    updateAdapterFromDb();
                    updateRevenue();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
        if (item == null) return; // footer case
        Log.d(TAG, "onListItemClick(). position: " + position + ", id: " + id + ", name: " + item.getName());

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
        ListViewItem item = (ListViewItem ) parent.getItemAtPosition(position);
        if (item == null) return true; // footer case
        Log.d(TAG, "onItemLongClick(). position: " + position + ", id: " + id + ", name: " + item.getFullName());
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

                if (mRefreshList.isEmpty()) {
                    mSwipeLayout.setRefreshing(false);
                    updateRevenue();
                }
            }
        }
    }

    private void refresh() {
        Log.d(TAG, "refresh()");
        synchronized (mRefreshLock) {
            mRefreshList = new ArrayList<>();
            BithumbClient bithumbClient = new BithumbClient(this);
            bithumbClient.execute();
            mRefreshList.add(Const.Exchange.BITHUMB);
            CoinoneClient coinoneClient = new CoinoneClient(this);
            coinoneClient.execute();
            mRefreshList.add(Const.Exchange.COINONE);
            KorbitClient korbitClient = new KorbitClient(this);
            korbitClient.execute();
            mRefreshList.add(Const.Exchange.KORBIT);
            CryptowatchClient cryptowatchClient = new CryptowatchClient(this);
            cryptowatchClient.execute();
            mRefreshList.add(Const.Exchange.CRYPTOWATCH);
            BitfinexClient bitfinexClient = new BitfinexClient(this);
            bitfinexClient.execute();
            mRefreshList.add(Const.Exchange.BITFINEX);

            // TODO: Find acceptable method
//            mRefreshList.add(Const.ExchangeRate.USDKRW);
//            UsdKrw usdKrw = new UsdKrw(this, this);
//            usdKrw.check();
//            mRefreshList.add(Const.ExchangeRate.USDJPY);
//            UsdJpy usdJpy = new UsdJpy(this, this);
//            usdJpy.check();
        }

        DbHelper dbHelper = DbHelper.getInstance(this);
        dbHelper.setUpdateTime(System.currentTimeMillis());
    }

    private void updateAdapterFromDb() {
        Log.d(TAG, "updateAdapterFromDb()");

        ListViewAdapter adapter = ListViewAdapter.getInstance();
        adapter.removeAllItem();

        DbHelper dbHelper = DbHelper.getInstance(this);
        ArrayList<CoinInfo> coinList = dbHelper.getInterestingCoinList(1);
        for (CoinInfo coinRow : coinList) {
            Log.i(TAG, coinRow.toString());
            ListViewItem item = new ListViewItem(coinRow.coinId, coinRow.coin, coinRow.currency, coinRow.exchange, coinRow.chartCoinone);
            item.setMyAvgPrice(coinRow.avgPrice);
            item.setMyQuantity(coinRow.quantity);
            adapter.addItem(item);
        }
        adapter.notifyDataSetChanged();
    }

    private void updateRevenue() {
        ListViewAdapter adapter = ListViewAdapter.getInstance();
        double[] initialPrice = {0.0d, 0.0d, 0.0d, 0.0d};  // KRW, USD, CNY, JPY
        double[] currentPrice = {0.0d, 0.0d, 0.0d, 0.0d};
        double curPrice, curInitPrice;
        for (int i = 0; i < adapter.getCount(); i++) {
            ListViewItem curItem = (ListViewItem)adapter.getItem(i);
            double myQuantity = curItem.getMyQuantity();
            if (myQuantity > 0.0d) {
                Log.d(TAG, "updateRevenue(). plus "  + curItem.getFullName());
                curPrice = curItem.getCurPrice() * curItem.getMyQuantity();
                curInitPrice = curItem.getMyAvgPrice() * curItem.getMyQuantity();
                String currency = curItem.getCurrency();
                switch (currency) {
                    case Const.Currency.KRW:
                        currentPrice[0] += curPrice;
                        initialPrice[0] += curInitPrice;
                        break;
                    case Const.Currency.USD:
                        currentPrice[1] += curPrice;
                        initialPrice[1] += curInitPrice;
                        break;
                    case Const.Currency.CNY:
                        currentPrice[2] += curPrice;
                        initialPrice[2] += curInitPrice;
                        break;
                    case Const.Currency.JPY:
                        currentPrice[3] += curPrice;
                        initialPrice[3] += curInitPrice;
                        break;
                    default:
                        Log.w(TAG, "updateRevenue(). unexpected currency. " + currency);
                        break;
                }
            }
        }
        if (initialPrice[0] == 0.0d && initialPrice[1] == 0.0d && initialPrice[2] == 0.0d && initialPrice[3] == 0.0d) {
            mFooter.setVisibility(View.GONE);
        } else {
            mFooter.setVisibility(View.VISIBLE);
            LinearLayout layoutKRW = (LinearLayout) mFooter.findViewById(R.id.layoutKRW);
            LinearLayout layoutUSD = (LinearLayout) mFooter.findViewById(R.id.layoutUSD);
            LinearLayout layoutCNY = (LinearLayout) mFooter.findViewById(R.id.layoutCNY);
            LinearLayout layoutJPY = (LinearLayout) mFooter.findViewById(R.id.layoutJPY);
            layoutKRW.setVisibility(View.GONE);
            layoutUSD.setVisibility(View.GONE);
            layoutCNY.setVisibility(View.GONE);
            layoutJPY.setVisibility(View.GONE);

            if (initialPrice[0] != 0.0d) {
                layoutKRW.setVisibility(View.VISIBLE);
                fillFooterForCurrecy(0, initialPrice, currentPrice, true);
            }
            if (initialPrice[1] != 0.0d) {
                layoutUSD.setVisibility(View.VISIBLE);
                fillFooterForCurrecy(1, initialPrice, currentPrice, false);
            }
            if (initialPrice[2] != 0.0d) {
                layoutCNY.setVisibility(View.VISIBLE);
                fillFooterForCurrecy(2, initialPrice, currentPrice, false);
            }
            if (initialPrice[3] != 0.0d) {
                layoutJPY.setVisibility(View.VISIBLE);
                fillFooterForCurrecy(3, initialPrice, currentPrice, false);
            }
        }
    }

    private void fillFooterForCurrecy(int index, double[] initialPrice, double[] currentPrice, boolean useIntCurrency) {
        if (index < 0 || index > 3) {
            Log.e(TAG, "fillFooterForCurrecy(). unexpected index. " + index);
            return;
        }
        TextView txt_revenuePercent = (TextView) mFooter.findViewById(R.id.txt_revenuePercentKRW);
        TextView txt_revenuePrice = (TextView) mFooter.findViewById(R.id.txt_revenuePriceKRW);
        TextView txt_currentPrice = (TextView) mFooter.findViewById(R.id.txt_currentPriceKRW);
        TextView txt_initialPrice = (TextView) mFooter.findViewById(R.id.txt_initialPriceKRW);
        String currencySymbol = "₩";
        switch (index) {
            case 0:
                // Do nothing. Proper views are already selected
                break;
            case 1:
                txt_revenuePercent = (TextView) mFooter.findViewById(R.id.txt_revenuePercentUSD);
                txt_revenuePrice = (TextView) mFooter.findViewById(R.id.txt_revenuePriceUSD);
                txt_currentPrice = (TextView) mFooter.findViewById(R.id.txt_currentPriceUSD);
                txt_initialPrice = (TextView) mFooter.findViewById(R.id.txt_initialPriceUSD);
                currencySymbol = "$";
                break;
            case 2:
                txt_revenuePercent = (TextView) mFooter.findViewById(R.id.txt_revenuePercentCNY);
                txt_revenuePrice = (TextView) mFooter.findViewById(R.id.txt_revenuePriceCNY);
                txt_currentPrice = (TextView) mFooter.findViewById(R.id.txt_currentPriceCNY);
                txt_initialPrice = (TextView) mFooter.findViewById(R.id.txt_initialPriceCNY);
                currencySymbol = "Ұ";
                break;
            case 3:
                txt_revenuePercent = (TextView) mFooter.findViewById(R.id.txt_revenuePercentJPY);
                txt_revenuePrice = (TextView) mFooter.findViewById(R.id.txt_revenuePriceJPY);
                txt_currentPrice = (TextView) mFooter.findViewById(R.id.txt_currentPriceJPY);
                txt_initialPrice = (TextView) mFooter.findViewById(R.id.txt_initialPriceJPY);
                currencySymbol = "¥";
                break;
            default:
                break;
        }

        String priceText;
        if (useIntCurrency) {
            priceText = mIntFormatter.format(initialPrice[index]);
            priceText = Util.priceWithSymbol(currencySymbol, priceText);
            txt_initialPrice.setText(priceText);

            priceText = mIntFormatter.format(currentPrice[index]);
            priceText = Util.priceWithSymbol(currencySymbol, priceText);
            txt_currentPrice.setText(priceText);

            priceText = mIntFormatter.format(currentPrice[index] - initialPrice[index]);
            priceText = Util.priceWithSymbol(currencySymbol, priceText);
            txt_revenuePrice.setText(priceText);
        } else {
            priceText = mFloatFormatter.format(initialPrice[index]);
            priceText = Util.priceWithSymbol(currencySymbol, priceText);
            txt_initialPrice.setText(priceText);

            priceText = mFloatFormatter.format(currentPrice[index]);
            priceText = Util.priceWithSymbol(currencySymbol, priceText);
            txt_currentPrice.setText(priceText);

            priceText = mFloatFormatter.format(currentPrice[index] - initialPrice[index]);
            priceText = Util.priceWithSymbol(currencySymbol, priceText);
            txt_revenuePrice.setText(priceText);
        }
        txt_revenuePercent.setText(mFloatFormatter.format((currentPrice[index] - initialPrice[index]) / initialPrice[index] * 100));
        if (currentPrice[index] - initialPrice[index] > 0.0d) {
            txt_currentPrice.setTextColor(Const.Color.LTRED);
            txt_revenuePrice.setTextColor(Const.Color.LTRED);
            txt_revenuePercent.setTextColor(Const.Color.LTRED);
        } else if (currentPrice[index] - initialPrice[index] < 0.0d) {
            txt_currentPrice.setTextColor(Const.Color.LTBLUE);
            txt_revenuePrice.setTextColor(Const.Color.LTBLUE);
            txt_revenuePercent.setTextColor(Const.Color.LTBLUE);
        }
    }
}
