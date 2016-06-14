package com.seniorcontrol.administrator.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

class HeadTitlePanel extends LinearLayout{
    private Context mContext;
    HeadTitlePanel(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        mContext = context;
        View parent = LayoutInflater.from(mContext).inflate(R.layout.head_title_panel, this, true);
    }

}