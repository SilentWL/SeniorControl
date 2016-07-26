package com.seniorcontrol.administrator.webview;

import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class WebViewLoadThread extends Thread{
    private WebView mWebView = null;
    private Handler mHandler = null;
    private URL mUrl = null;
    public WebViewLoadThread(WebView webView, Handler handler, String url) throws MalformedURLException {
        mWebView = webView;
        mHandler = handler;
        mUrl = new URL(url);
    }

    @Override
    public void run() {
        //super.run();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String tempString;
            while ((tempString = bufferedReader.readLine()) != null){
                stringBuffer.append(tempString);
            }

            if (stringBuffer.length() > 0){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadData(stringBuffer.toString(), "text/html;charset=utf-8", null);
                        //mWebView.loadUrl(mUrl.toString());

                        mWebView.setWebViewClient(new WebViewClient(){

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                //super.shouldOverrideUrlLoading(view, url);
                                return true;
                            }
                        });
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
