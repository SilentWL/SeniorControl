package com.download.service;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.download.constant.ConstantDatas;
import com.download.sqlhelper.SqlOperation;
import com.download.utils.FileInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class FilePiecesDownloadThread extends Thread{
    private Handler mHandler;
    private Context mContext;
    private FileInfo mFileInfo;
    private static final String TAG = "FilePiecesDownload";

    public void setmStopThread(boolean mStopThread) {
        this.mStopThread = mStopThread;
    }

    private  boolean mStopThread = false;

    public FilePiecesDownloadThread(Context Context, FileInfo FileInfo, Handler Handler) {
        this.mContext = Context;
        this.mFileInfo = FileInfo;
        this.mHandler = Handler;
    }

    @Override
    public void run() {
        URL url = null;
        HttpURLConnection conn = null;
        mFileInfo.setThreadId(getId());
        RandomAccessFile rf = null;
        InputStream in = null;
        SqlOperation.getInstance(mContext).writeFileInfoThreadIdToDB(mFileInfo);

        try {
            url = new URL(mFileInfo.getUrl());
            conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Range", "bytes=" + (mFileInfo.getStart() + mFileInfo.getCursor()) + "-" + mFileInfo.getEnd());

                in = conn.getInputStream();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                    File dir = new File(ConstantDatas.DOWNLOAD_DIR);

                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    long cursor = mFileInfo.getCursor() + mFileInfo.getStart();
                    int readLength = 0;
                    File desFile = new File(dir, getFileName(mFileInfo.getUrl()));

                    rf = new RandomAccessFile(desFile, "rwd");
                    rf.seek(cursor);

                    byte readBytes[] = new byte[1024 * 1024 * 10];

                    Long currentTime = System.currentTimeMillis();
                    while (!mStopThread && (readLength = in.read(readBytes, 0, 1024 * 1024 * 10)) > 0) {
                        rf.write(readBytes, 0, readLength);
                        cursor += readLength;
                        mFileInfo.setCursor(cursor - mFileInfo.getStart());

                        if (System.currentTimeMillis() - currentTime >= 1000){
                            mHandler.obtainMessage(ConstantDatas.DOWNLOAD_SERVICE_DOWNLOADING_MSG, mFileInfo).sendToTarget();
                            //SqlOperation.getInstance(mContext).writeFileInfoToDBByThreadId(mFileInfo);
                            currentTime = System.currentTimeMillis();
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            SqlOperation.getInstance(mContext).writeFileInfoToDBByThreadId(mFileInfo);
            mHandler.obtainMessage(ConstantDatas.DOWNLOAD_SERVICE_FILE_PIECE_FINISH_MSG, mFileInfo).sendToTarget();
            try {
                if (rf != null){
                    rf.close();
                }
                if (in != null){
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
