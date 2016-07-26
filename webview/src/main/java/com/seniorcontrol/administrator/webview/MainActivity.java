package com.seniorcontrol.administrator.webview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView = null;
    private ImageView mImageView =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.WebView);
        mImageView = (ImageView) findViewById(R.id.ImageView);

        try {
            //new WebViewLoadThread(mWebView, new Handler(), "http://baidu.com").start();
            new ImageViewLoadImageThread(mImageView, new Handler(), "http://imgst-dl.meilishuo.net/pic/_o/84/a4/a30be77c4ca62cd87156da202faf_1440_900.jpg").start();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
