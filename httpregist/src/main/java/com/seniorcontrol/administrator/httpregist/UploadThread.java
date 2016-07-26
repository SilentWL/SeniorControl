package com.seniorcontrol.administrator.httpregist;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class UploadThread extends Thread{
    private URL url;
    private String fileName;
    private Handler handler;

    public UploadThread(String fileName, String url, Handler handler) {
        this.handler = handler;
        this.fileName = fileName;
        try {
            this.url = new URL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String boundary = "----WebKitFormBoundaryf5YJRKuaBvnB1WVe";
        String prefix = "--";
        String end="\r\n";
        //super.run();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            outputStream.writeBytes(prefix + boundary + end);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"abc.mp3\"" + end);
            outputStream.writeBytes("Content-Type: audio/mp3" + end);
            outputStream.writeBytes(end);
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));

            byte[] b = new byte[4*1024];
            int len;

            while((len = fileInputStream.read(b)) != -1){
                outputStream.write(b, 0, len);
            }
            outputStream.writeBytes(end);
            outputStream.writeBytes(prefix + boundary + prefix + end);
            outputStream.flush();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String response;
            while ((response = bufferedReader.readLine()) != null){
                stringBuffer.append(response);
            }
            if (outputStream != null){
                outputStream.close();
            }
            if (bufferedReader != null){
                bufferedReader.close();
            }
            handler.sendEmptyMessage(200);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
