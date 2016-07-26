package com.seniorcontrol.administrator.eventbus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class ItemListFragment extends ListFragment{
    private Handler mHander = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mHander = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                EventBus.getDefault().post(Item.ITEMS);

            }
        };

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(){

            @Override
            public void run() {
                try{
                    Thread.sleep(5000);
                    mHander.sendEmptyMessage(0);
                    //EventBus.getDefault().post(new Event.ItemListEvent(Item.ITEMS));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        EventBus.getDefault().post(getListView().getItemAtPosition(position));
    }
    @Subscribe
    public void onEventMainThread(List<Item> items){

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, items);
        setListAdapter(adapter);

    }
}
