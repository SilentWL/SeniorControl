package com.download.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.download.constant.ConstantDatas;
import com.download.service.BackgroundDownloadService;
import com.seniorcontrol.administrator.upload_from_break.R;

public class MainActivity extends AppCompatActivity {
    private TextView mfileNameTv;
    private ProgressBar mDownLoadPb;
    private Button mStartDownLoadBt, mPauseDownLoadBt;
    private TextView mfileNameTv1;
    private ProgressBar mDownLoadPb1;
    private Button mStartDownLoadBt1, mPauseDownLoadBt1;

    private TextView mfileNameTv2;
    private ProgressBar mDownLoadPb2;
    private Button mStartDownLoadBt2, mPauseDownLoadBt2;
    private BroadcastReceiver bc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConstantDatas.UI_UPDATE_PROGRESS_BROADCAST_ACTION)){
                //mDownLoadPb.setProgress(intent.getIntExtra(ConstantDatas.UI_UPDATE_PROGRESS_DATA, BackgroundDownloadService.getDownloadProgress(MainActivity.this, intent.getStringExtra(ConstantDatas.UI_UPDATE_PROGRESS_KEY_URL))));
                if (intent.getStringExtra(ConstantDatas.UI_UPDATE_PROGRESS_KEY_URL).equals("http://www.imooc.com/mobile/mukewang.apk")) {
                    mDownLoadPb.setProgress(intent.getIntExtra(ConstantDatas.UI_UPDATE_PROGRESS_DATA, 0));
                }else if (intent.getStringExtra(ConstantDatas.UI_UPDATE_PROGRESS_KEY_URL).equals("http://192.168.199.18:8080/Web/abc.zip")){
                    mDownLoadPb1.setProgress(intent.getIntExtra(ConstantDatas.UI_UPDATE_PROGRESS_DATA, 0));
                }else if (intent.getStringExtra(ConstantDatas.UI_UPDATE_PROGRESS_KEY_URL).equals("http://192.168.199.18:8080/Web/abc.mp")){
                    mDownLoadPb2.setProgress(intent.getIntExtra(ConstantDatas.UI_UPDATE_PROGRESS_DATA, 0));
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPauseDownLoadBt = (Button) findViewById(R.id.Pause);
        mStartDownLoadBt = (Button) findViewById(R.id.Start);
        mDownLoadPb = (ProgressBar) findViewById(R.id.DownloadProgress);
        mDownLoadPb.setMax(100);

        mPauseDownLoadBt1 = (Button) findViewById(R.id.Pause1);
        mStartDownLoadBt1 = (Button) findViewById(R.id.Start1);
        mDownLoadPb1 = (ProgressBar) findViewById(R.id.DownloadProgress1);

        mPauseDownLoadBt2 = (Button) findViewById(R.id.Pause2);
        mStartDownLoadBt2 = (Button) findViewById(R.id.Start2);
        mDownLoadPb2 = (ProgressBar) findViewById(R.id.DownloadProgress2);
        IntentFilter intentFilter = new IntentFilter(ConstantDatas.UI_UPDATE_PROGRESS_BROADCAST_ACTION);
        registerReceiver(bc, intentFilter);

        mStartDownLoadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(MainActivity.this, BackgroundDownloadService.class);
                intent.putExtra(ConstantDatas.START_DOWNLOAD_INTENT_KEY_URL, "http://www.imooc.com/mobile/mukewang.apk");
                intent.setAction(ConstantDatas.DOWNLOAD_SERVICE_START_DOWNLOAD);

                startService(intent);
            }
        });

        mPauseDownLoadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(MainActivity.this, BackgroundDownloadService.class);
                intent.putExtra(ConstantDatas.START_DOWNLOAD_INTENT_KEY_URL, "http://www.imooc.com/mobile/mukewang.apk");
                intent.setAction(ConstantDatas.DOWNLOAD_SERVICE_STOP_DOWNLOAD);

                startService(intent);
            }
        });
        mStartDownLoadBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(MainActivity.this, BackgroundDownloadService.class);
                intent.putExtra(ConstantDatas.START_DOWNLOAD_INTENT_KEY_URL, "http://192.168.199.18:8080/Web/abc.zip");
                intent.setAction(ConstantDatas.DOWNLOAD_SERVICE_START_DOWNLOAD);

                startService(intent);
            }
        });

        mPauseDownLoadBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(MainActivity.this, BackgroundDownloadService.class);
                intent.putExtra(ConstantDatas.START_DOWNLOAD_INTENT_KEY_URL, "http://192.168.199.18:8080/Web/abc.zip");
                intent.setAction(ConstantDatas.DOWNLOAD_SERVICE_STOP_DOWNLOAD);

                startService(intent);
            }
        });
        mStartDownLoadBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(MainActivity.this, BackgroundDownloadService.class);
                intent.putExtra(ConstantDatas.START_DOWNLOAD_INTENT_KEY_URL, "http://192.168.199.18:8080/Web/abc.mp");
                intent.setAction(ConstantDatas.DOWNLOAD_SERVICE_START_DOWNLOAD);

                startService(intent);
            }
        });

        mPauseDownLoadBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(MainActivity.this, BackgroundDownloadService.class);
                intent.putExtra(ConstantDatas.START_DOWNLOAD_INTENT_KEY_URL, "http://192.168.199.18:8080/Web/abc.mp");
                intent.setAction(ConstantDatas.DOWNLOAD_SERVICE_STOP_DOWNLOAD);

                startService(intent);
            }
        });

    }
}
