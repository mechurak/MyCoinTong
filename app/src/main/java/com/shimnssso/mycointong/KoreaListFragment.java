package com.shimnssso.mycointong;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shimnssso.mycointong.network.BithumClient;
import com.shimnssso.mycointong.network.CoinoneClient;
import com.shimnssso.mycointong.network.KorbitClient;

public class KoreaListFragment extends ListFragment {
    private static final String TAG = "KoreaListFragment";
    ListViewAdapter adapter ;

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // get TextView's Text.
        ListViewItem item = (ListViewItem ) l.getItemAtPosition(position) ;
        Log.d(TAG, "onListItemClick(). position: " + position + ", id: " + id + "name: " + item.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = ListViewAdapter.getInstance() ;
        setListAdapter(adapter) ;

        ListViewItem korbitItem = new ListViewItem(Constant.CoinName.BTC_KORBIT);
        adapter.addItem(korbitItem);

        ListViewItem bithumItem = new ListViewItem(Constant.CoinName.BTC_BITHUM);
        adapter.addItem(bithumItem);

        ListViewItem coinoneItem = new ListViewItem(Constant.CoinName.BTC_COINONE);
        adapter.addItem(coinoneItem);

        ListViewItem ethBithumItem = new ListViewItem(Constant.CoinName.ETH_BITHUM);
        adapter.addItem(ethBithumItem);

        ListViewItem ethCoinoneItem = new ListViewItem(Constant.CoinName.ETH_COINONE);
        adapter.addItem(ethCoinoneItem);

        ListViewItem xrpBithumItem = new ListViewItem(Constant.CoinName.XRP_BITHUM);
        adapter.addItem(xrpBithumItem);

        ListViewItem xrpCoinoneItem = new ListViewItem(Constant.CoinName.XRP_COINONE);
        adapter.addItem(xrpCoinoneItem);


        // TODO: Remove this test code
        BithumClient bithumClient = new BithumClient();
        bithumClient.execute();
        CoinoneClient coinoneClient = new CoinoneClient();
        coinoneClient.execute();
        KorbitClient korbitClient = new KorbitClient();
        korbitClient.execute();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
