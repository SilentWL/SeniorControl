package com.seniorcontrol.administrator.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class MainActivity extends AppCompatActivity implements HorizontalProgressBar.RefreshData{

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 100){
                if ((progress += 1) <= 100) {
                    horizontalProgressBar.setProgress(progress);
                    handler.sendEmptyMessageDelayed(100, 100);
                }
            }
        }
    };
    HorizontalProgressBar horizontalProgressBar = null;
    int progress = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        horizontalProgressBar = (HorizontalProgressBar)findViewById(R.id.progress);
        progress = horizontalProgressBar.getProgress();
        handler.handleMessage(Message.obtain(handler, 100));
        horizontalProgressBar.SetRefreshDateInterface(this);
    }


    @Override
    public void onRefreshData() {

    }
}
