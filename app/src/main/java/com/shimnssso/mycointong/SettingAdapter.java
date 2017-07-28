package com.shimnssso.mycointong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class SettingAdapter extends BaseAdapter {
    private static final String TAG = "SettingAdapter";

    private ArrayList<String> listViewItemList = new ArrayList<>() ;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        String coinFullName = listViewItemList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_setting, parent, false);
        }

        CheckBox chk_coinName = (CheckBox) convertView.findViewById(R.id.chk_coinName);
        Button btn_up = (Button) convertView.findViewById(R.id.btn_up);
        Button btn_down = (Button) convertView.findViewById(R.id.btn_down);

        chk_coinName.setText(coinFullName);

        return convertView;
    }

    void addItem(String coinFullName) {
        listViewItemList.add(coinFullName);
    }
}
