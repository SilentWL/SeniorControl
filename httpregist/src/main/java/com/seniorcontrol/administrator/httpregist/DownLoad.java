package com.seniorcontrol.administrator.httpregist;

import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class DownLoad{
    private Executor threadPool = Executors.newFixedThreadPool(3);
    private Handler handler;

    public DownLoad(Handler handler) {
        this.handler = handler;
    }

    class DownLoadRunnable implements Runnable{
        private String url;
        private String fileName;
        private long start;
        private long end;
        public DownLoadRunnable(String url, String fileName, long start, long end){
            this.url = url;
            this.fileName = fileName;
            this.start = start;
            this.end = end;
        }
        @Override
        public void run() {
            try {
                URL httpUrl = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();;
                httpURLConnection.setRequestProperty("Range", "bytes=" + start + "-" + end);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                RandomAccessFile accessFile = new RandomAccessFile(new File(fileName), "rwd");
                accessFile.seek(start);

                InputStream in = httpURLConnection.getInputStream();
                byte[] b = new byte[1024 * 4];
                int len;
                while ((len = in.read(b)) != -1){
                    accessFile.write(b, 0, len);
                }
                if (accessFile != null){
                    accessFile.close();
                }
                if (in != null){
                    in.close();
                }
                handler.sendEmptyMessage(100);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void downLoadFile(String url){
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            int count = httpURLConnection.getContentLength();
            int block = count / 3;
            String fileName = getFileName(url);
            File parent = Environment.getExternalStorageDirectory();
            File fileDownLoad = new File(parent, fileName);

            for(int i = 0; i < 3; i++){
                long start = i * block;
                long end = (i + 1) * block - 1;

                if (i == 2){
                    end = count;
                }
                DownLoadRunnable downLoadRunnable = new DownLoadRunnable(url, fileDownLoad.getAbsolutePath(), start, end);
                threadPool.execute(downLoadRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getFileName(String url){
        return url.substring(url.lastIndexOf("/") + 1);
    }

}
