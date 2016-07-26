package com.seniorcontrol.administrator.httpregist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class HttpImage extends Thread{
    private Handler handler;
    private ImageView imageView;
    private URL url;
    public HttpImage(String url, Handler handler, ImageView imageView) {
        //super();
        this.handler = handler;
        this.imageView = imageView;
        try {
            this.url = new URL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //super.run();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            InputStream inputStream = httpURLConnection.getInputStream();

            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap != null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
