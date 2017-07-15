package com.shimnssso.mycointong;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
        TextView priceTextView = (TextView) convertView.findViewById(R.id.price);
        TextView volumeTextView = (TextView) convertView.findViewById(R.id.volume);
        TextView priceChangeTextView = (TextView) convertView.findViewById(R.id.priceChange);
        TextView percentChangeTextView = (TextView) convertView.findViewById(R.id.percentChange);
        TextView myPriceChangeTextView = (TextView) convertView.findViewById(R.id.myPriceChange);
        TextView myPercentChangeTextView = (TextView) convertView.findViewById(R.id.myPercentChange);
        CandleView candleView = (CandleView) convertView.findViewById(R.id.candle);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(listViewItem.getName());
        DecimalFormat intFormatter = new DecimalFormat("#,###");
        DecimalFormat floatFormatter = new DecimalFormat("#,##0.00");

        priceTextView.setText(intFormatter.format(listViewItem.getCurPrice()));
        volumeTextView.setText(intFormatter.format(listViewItem.getVolume()));
        int changePrice = listViewItem.getCurPrice() - listViewItem.getOpenPrice();
        float changePercent = 0.0f;
        if (listViewItem.getOpenPrice() > 0.0f) {
            changePercent = (float)changePrice / listViewItem.getOpenPrice() * 100;
        }
        priceChangeTextView.setText(intFormatter.format(changePrice));
        percentChangeTextView.setText(floatFormatter.format(changePercent));
        if (changePrice > 0) {
            priceTextView.setTextColor(Color.RED);
            priceChangeTextView.setTextColor(Color.RED);
            percentChangeTextView.setTextColor(Color.RED);
        } else if (changePrice < 0) {
            priceTextView.setTextColor(Color.BLUE);
            priceChangeTextView.setTextColor(Color.BLUE);
            percentChangeTextView.setTextColor(Color.BLUE);
        } else {
            priceTextView.setTextColor(Color.BLACK);
            priceChangeTextView.setTextColor(Color.BLACK);
            percentChangeTextView.setTextColor(Color.BLACK);
        }

        double myPriceChange = 0;
        if (listViewItem.getMyAvgPrice() != 0.0d) {
            myPriceChange = listViewItem.getCurPrice() - listViewItem.getMyAvgPrice();
        }
        double myPercentChange = 0.0d;
        if (myPriceChange != 0 && listViewItem.getMyAvgPrice() > 0.0d) {
            myPercentChange = myPriceChange / listViewItem.getCurPrice() * 100;

            myPriceChangeTextView.setText(intFormatter.format(myPriceChange));
            myPercentChangeTextView.setText(floatFormatter.format(myPercentChange));
        }
        else {
            myPriceChangeTextView.setText("");
            myPercentChangeTextView.setText("");
        }
        if (myPriceChange > 0) {
            myPriceChangeTextView.setTextColor(Color.RED);
            myPercentChangeTextView.setTextColor(Color.RED);
        } else if (myPriceChange < 0) {
            myPriceChangeTextView.setTextColor(Color.BLUE);
            myPercentChangeTextView.setTextColor(Color.BLUE);
        } else {
            myPriceChangeTextView.setTextColor(Color.BLACK);
            myPercentChangeTextView.setTextColor(Color.BLACK);
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

    public Object getItemByName(String name) {
        for (ListViewItem item : listViewItemList) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    void addItem(ListViewItem item) {
        listViewItemList.add(item);
    }
}