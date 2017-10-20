package com.shimnssso.mycointong.setting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.shimnssso.mycointong.R;

import java.util.ArrayList;
import java.util.Iterator;

class SettingAdapter extends BaseAdapter {
    private static final String TAG = "SettingAdapter";

    private ArrayList<CoinItem> listViewItemList = new ArrayList<>();
    private int mSelectCount = 0;
    private SelectListener mListener;

    public SettingAdapter(SelectListener listener) {
        super();
        mListener = listener;
    }

    public ArrayList<Integer> getCoinIdList() {
        ArrayList<Integer> retList = new ArrayList<>();
        for (CoinItem coinItem : listViewItemList) {
            retList.add(coinItem.getCoinId());
        }
        return retList;
    }

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

        final CheckBox chk_coinName = (CheckBox) convertView.findViewById(R.id.chk_coinName);
        chk_coinName.setText(coinItem.getCoinFullName());
        chk_coinName.setChecked(coinItem.isSelected());
        chk_coinName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coinItem.isSelected()) {
                    coinItem.setSelected(false);
                    mSelectCount--;
                } else {
                    coinItem.setSelected(true);
                    mSelectCount++;
                }
                chk_coinName.setSelected(coinItem.isSelected());
                if (mListener != null) {
                    mListener.onSelectCountChanged(mSelectCount);
                }
            }
        });
        return convertView;
    }

    public static final int BTN_TYPE_UP = 1;
    public static final int BTN_TYPE_DOWN = 0;
    void handleUpDown(int type) {
        int position = 0;
        CoinItem coinItem = null;
        Iterator<CoinItem> i = listViewItemList.iterator();
        while(i.hasNext()) {
            coinItem = i.next();
            if (coinItem.isSelected()) {
                Log.d(TAG, "selected item. " + coinItem.getCoinFullName() + ", position: " + position);
                break;
            }
            position++;
        }
        if (coinItem == null) {
            Log.e(TAG, "coinItem is null!!!");
            return;
        }

        if (type == BTN_TYPE_UP) {
            int targetIndex = position - 1;
            Log.d(TAG, "btn_up. targetIndex: " + targetIndex + ", size: " + listViewItemList.size());
            if (targetIndex >= 0) {
                listViewItemList.remove(coinItem);
                listViewItemList.add(targetIndex, coinItem);
                notifyDataSetChanged();
            } else {
                Log.d(TAG, "btn_up. very top");
            }
        } else if(type == BTN_TYPE_DOWN) {
            int targetIndex = position + 1;
            Log.d(TAG, "btn_down. targetIndex: " + targetIndex + ", size: " + listViewItemList.size());
            if (targetIndex < listViewItemList.size()) {
                listViewItemList.remove(coinItem);
                listViewItemList.add(targetIndex, coinItem);
                notifyDataSetChanged();
            } else {
                Log.d(TAG, "btn_down. very bottom");
            }
        } else {
            Log.e(TAG, "unexpected type. " + type);
        }
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
                mSelectCount--;
                count++;
            }
        }
        if (count > 0) {
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.onSelectCountChanged(mSelectCount);
            }
        } else {
            Log.w(TAG, "There are no items selected.");
        }
        return count;
    }

    public interface SelectListener {
        void onSelectCountChanged(int selectCount);
    }
}
