package com.shimnssso.mycointong;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = "ListViewAdapter";

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>() ;

    private static ListViewAdapter mInstance;
    public static ListViewAdapter getInstance() {
        if (mInstance == null) {
            mInstance = new ListViewAdapter();
        }
        return mInstance;
    }

    private ListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
        TextView exchangeTextView = (TextView) convertView.findViewById(R.id.exchange);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.price);
        TextView volumeTextView = (TextView) convertView.findViewById(R.id.volume);
        TextView priceChangeTextView = (TextView) convertView.findViewById(R.id.priceChange);
        TextView percentChangeTextView = (TextView) convertView.findViewById(R.id.percentChange);
        TextView myPriceChangeTextView = (TextView) convertView.findViewById(R.id.myPriceChange);
        TextView myPercentChangeTextView = (TextView) convertView.findViewById(R.id.myPercentChange);
        CandleView candleView = (CandleView) convertView.findViewById(R.id.candle);
        int originTextColor = nameTextView.getTextColors().getDefaultColor();
        int originBgColor = convertView.getSolidColor();

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.BLACK);
        } else {
            convertView.setBackgroundColor(originBgColor);
        }

        String currencySymbol = Util.getCurrenySymbol(listViewItem.getCurrency());

        // 아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(listViewItem.getName());
        exchangeTextView.setText(listViewItem.getExchange());
        DecimalFormat intFormatter = new DecimalFormat("#,###");
        DecimalFormat floatFormatter = new DecimalFormat("#,##0.00");
        boolean useIntCurrency = Util.useIntCurrency(listViewItem.getCurrency());
        double changePrice = listViewItem.getCurPrice() - listViewItem.getOpenPrice();

        String priceText, priceChangeText;
        if (useIntCurrency) {
            priceText = intFormatter.format(listViewItem.getCurPrice());
            priceChangeText = intFormatter.format(changePrice);
        } else {
            priceText = floatFormatter.format(listViewItem.getCurPrice());
            priceChangeText = floatFormatter.format(changePrice);
        }
        priceText = Util.priceWithSymbol(currencySymbol, priceText);
        priceChangeText = Util.priceWithSymbol(currencySymbol, priceChangeText);

        priceTextView.setText(priceText);
        volumeTextView.setText(intFormatter.format(listViewItem.getVolume()));
        float changePercent = 0.0f;
        if (listViewItem.getOpenPrice() > 0.0f) {
            changePercent = (float)(changePrice / listViewItem.getOpenPrice() * 100);
        }
        priceChangeTextView.setText(priceChangeText);
        percentChangeTextView.setText(floatFormatter.format(changePercent));
        if (changePrice > 0) {
            priceTextView.setTextColor(Const.Color.LTRED);
            priceChangeTextView.setTextColor(Const.Color.LTRED);
            percentChangeTextView.setTextColor(Const.Color.LTRED);
        } else if (changePrice < 0) {
            priceTextView.setTextColor(Const.Color.LTBLUE);
            priceChangeTextView.setTextColor(Const.Color.LTBLUE);
            percentChangeTextView.setTextColor(Const.Color.LTBLUE);
        } else {
            priceTextView.setTextColor(originTextColor);
            priceChangeTextView.setTextColor(originTextColor);
            percentChangeTextView.setTextColor(originTextColor);
        }

        double myPriceChange = 0.0d;
        if (listViewItem.getMyAvgPrice() != 0.0d) {
            myPriceChange = listViewItem.getCurPrice() - listViewItem.getMyAvgPrice();
        }

        if (myPriceChange != 0 && listViewItem.getMyAvgPrice() > 0.0d) {
            float myPercentChange = (float)(myPriceChange / listViewItem.getMyAvgPrice() * 100);
            double myProfit = myPriceChange * listViewItem.getMyQuantity();
            String myPriceChangeText;
            if (useIntCurrency) {
                myPriceChangeText = intFormatter.format(myProfit);
            } else {
                myPriceChangeText = floatFormatter.format(myProfit);
            }
            myPriceChangeText = Util.priceWithSymbol(currencySymbol, myPriceChangeText);
            myPriceChangeTextView.setText(myPriceChangeText);
            myPercentChangeTextView.setText(floatFormatter.format(myPercentChange));
        }
        else {
            myPriceChangeTextView.setText("");
            myPercentChangeTextView.setText("");
        }
        if (myPriceChange > 0) {
            myPriceChangeTextView.setTextColor(Const.Color.LTRED);
            myPercentChangeTextView.setTextColor(Const.Color.LTRED);
        } else if (myPriceChange < 0) {
            myPriceChangeTextView.setTextColor(Const.Color.LTBLUE);
            myPercentChangeTextView.setTextColor(Const.Color.LTBLUE);
        } else {
            myPriceChangeTextView.setTextColor(originTextColor);
            myPercentChangeTextView.setTextColor(originTextColor);
        }

        candleView.setData(listViewItem.getOpenPrice(), listViewItem.getHighPrice(), listViewItem.getLowPrice(), listViewItem.getCurPrice());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public Object getItemByName(String coin, String currency, String exchange) {
        for (ListViewItem item : listViewItemList) {
            if (item.getCoin().equals(coin) && item.getCurrency().equals(currency) && item.getExchange().equals(exchange)) {
                return item;
            }
        }
        return null;
    }

    void addItem(ListViewItem item) {
        listViewItemList.add(item);
    }

    void removeAllItem() {
        listViewItemList.clear();
    }
}