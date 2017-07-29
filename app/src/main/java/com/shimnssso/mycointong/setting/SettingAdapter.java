package com.shimnssso.mycointong.setting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.shimnssso.mycointong.R;

import java.util.ArrayList;
import java.util.Iterator;

class SettingAdapter extends BaseAdapter {
    private static final String TAG = "SettingAdapter";

    private ArrayList<CoinItem> listViewItemList = new ArrayList<>() ;

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final CoinItem coinItem = listViewItemList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_setting, parent, false);
        }

        CheckBox chk_coinName = (CheckBox) convertView.findViewById(R.id.chk_coinName);
        chk_coinName.setChecked(coinItem.isSelected());
        chk_coinName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                coinItem.setSelected(isChecked);
            }
        });
        Button btn_up = (Button) convertView.findViewById(R.id.btn_up);
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int targetIndex = position - 1;
                Log.d(TAG, "btn_up. targetIndex: " + targetIndex + ", size: " + listViewItemList.size());
                if (targetIndex >= 0) {
                    listViewItemList.remove(coinItem);
                    listViewItemList.add(targetIndex, coinItem);
                    notifyDataSetChanged();
                } else {
                    Log.d(TAG, "btn_up. very top");
                }
            }
        });
        Button btn_down = (Button) convertView.findViewById(R.id.btn_down);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int targetIndex = position + 1;
                Log.d(TAG, "btn_down. targetIndex: " + targetIndex + ", size: " + listViewItemList.size());
                if (targetIndex < listViewItemList.size()) {
                    listViewItemList.remove(coinItem);
                    listViewItemList.add(targetIndex, coinItem);
                    notifyDataSetChanged();
                } else {
                    Log.d(TAG, "btn_down. very bottom");
                }
            }
        });

        chk_coinName.setText(coinItem.getCoinFullName());

        return convertView;
    }

    void addItem(CoinItem coinItem) {
        listViewItemList.add(coinItem);
    }

    int deleteSelected() {
        int count = 0;
        Iterator<CoinItem> i = listViewItemList.iterator();
        while(i.hasNext()) {
            CoinItem coinItem = i.next();
            if (coinItem.isSelected()) {
                Log.d(TAG, coinItem.getCoinFullName() + " is deleting");
                i.remove();
                count++;
            }
        }
        if (count > 0) {
            notifyDataSetChanged();
        } else {
            Log.w(TAG, "There are no items selected.");
        }
        return count;
    }
}
