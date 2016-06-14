package com.seniorcontrol.administrator.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MainFragment fragment = new MainFragment();
        Bundle b = new Bundle();
        String content = Constant.CONTENT[position];
        b.putString("TITLES", content);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getCount() {
        return Constant.TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constant.TITLES[position];
    }
}
