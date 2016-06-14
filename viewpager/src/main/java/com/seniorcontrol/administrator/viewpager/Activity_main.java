package com.seniorcontrol.administrator.viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class Activity_main extends FragmentActivity{
    private TabPageIndicator mPageIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter fragPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_main);
ADF
        mPageIndicator = (TabPageIndicator)findViewById(R.id.page_indicator);
        mViewPager = (ViewPager)findViewById(R.id.view_pager);

        fragPagerAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(fragPagerAdapter);
        mPageIndicator.setViewPager(mViewPager, 0);
    }


    private void setTranslucentStatus()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            Window window = getWindow();

            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#e0e0e0"));
            //window.setNavigationBarColor(Color.TRANSPARENT);
        }

    }
}
