package com.shimnssso.mycointong.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.shimnssso.mycointong.R;

import java.util.ArrayList;

public class AddAdapter extends BaseAdapter {
    private static final String TAG = "AddAdapter";

    private ArrayList<CoinItem> items = new ArrayList<>();

    public ArrayList<String> getSeletedCoinFullNameList() {
        ArrayList<String> retList = new ArrayList<>();
        for (CoinItem coinItem : items) {
            if (coinItem.isSelected()) {
                retList.add(coinItem.getCoinFullName());
            }
        }
        return retList;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final CoinItem coinItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_add, parent, false);
        }

        CheckBox chk_coinName = (CheckBox) convertView.findViewById(R.id.chk_coinName);
        chk_coinName.setText(coinItem.getCoinFullName());
        chk_coinName.setChecked(coinItem.isSelected());
        chk_coinName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                coinItem.setSelected(isChecked);
            }
        });

        return convertView;
    }

    void addItem(CoinItem coinItem) {
        items.add(coinItem);
    }
}
