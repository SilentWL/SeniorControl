package com.seniorcontrol.administrator.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class MainFragment extends Fragment {
    private int mNewsType = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_item_fragment_main, null);
        TextView tip = (TextView) v.findViewById(R.id.id_tip);
        Bundle b = getArguments();
        String title = b.getString("TITLES");
        tip.setText(title);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
