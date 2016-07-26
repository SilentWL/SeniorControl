package com.seniorcontrol.administrator.httpregist;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class SubmitThread extends Thread{
    private Handler handler;
    private Context context;
    private URL url;
    private String name, age;
    private boolean get;
    public SubmitThread(Handler handler, Context context, String url, String name, String age, boolean get) throws MalformedURLException, UnsupportedEncodingException {
        this.handler = handler;
        this.context = context;

        if ((this.get = get) == true) {
            String urlText = url + "?" + "name=" + URLEncoder.encode(name, "utf-8") + "&" + "age=" + age;
            this.url = new URL(urlText);
        }
        else {
            this.url = new URL(url);
        }
        this.name = name;
        this.age = age;
    }

    @Override
    public void run() {
        //super.run();
        if (get){
            doGetMethod();
        }else
        {
            doPostMethod();
        }
    }

    private void doGetMethod() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String response;
            if ((response = bufferedReader.readLine()) != null){
                stringBuffer.append(response);
            }

            if (stringBuffer.length() != 0){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doPostMethod() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(5000);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            String content = "name=" + name + "&age=" + age;
            outputStream.write(content.getBytes());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String response;
            if ((response = bufferedReader.readLine()) != null){
                stringBuffer.append(response);
            }

            if (stringBuffer.length() != 0){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
