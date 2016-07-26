package com.seniorcontrol.administrator.webview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class ImageViewLoadImageThread extends Thread{
    private ImageView mImageView = null;
    private Handler mHandler = null;
    private URL mUrl = null;
    public ImageViewLoadImageThread(ImageView imageView, Handler handler, String url) throws MalformedURLException {
        mImageView = imageView;
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
            httpURLConnection.setDoInput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            FileOutputStream out = null;
            File downLoadFile = null;


            String fileName = String.valueOf(System.currentTimeMillis());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = Environment.getExternalStorageDirectory();

                downLoadFile = new File(file, fileName);

                out = new FileOutputStream(downLoadFile);
            }
            byte[] b = new byte[1024];

            int len;

            if (out!= null){
                while ((len = inputStream.read(b)) != -1){
                    out.write(b, 0, len);
                }
            }
            final Bitmap bitmap = BitmapFactory.decodeFile(downLoadFile.getAbsolutePath());

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmap(bitmap);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
