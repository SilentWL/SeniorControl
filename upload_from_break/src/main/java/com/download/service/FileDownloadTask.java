package com.download.service;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.download.sqlhelper.SqlOperation;
import com.download.utils.FileInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class FileDownloadTask {
    private static ExecutorService mThreadPool = Executors.newCachedThreadPool();
    private Context mContext;
    private Handler mHandler;
    private List<FileInfo> mfileInfos;
    private List<FilePiecesDownloadThread> mFilePiecesDownloadThreads = new ArrayList<FilePiecesDownloadThread>();
    private static final String TAG = "FileDownloadTask";


    public FileDownloadTask(List<FileInfo> fileInfos, Context mContext, Handler mHandler) {
        this.mfileInfos = fileInfos;
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    public void downloadFile(){
        for (FileInfo fileInfo : mfileInfos) {
            if (fileInfo.getEnd() - fileInfo.getStart() + 1 > fileInfo.getCursor()) {
                FilePiecesDownloadThread filePiecesDownloadThread = new FilePiecesDownloadThread(mContext, fileInfo, mHandler);
                mFilePiecesDownloadThreads.add(filePiecesDownloadThread);
                mThreadPool.execute(filePiecesDownloadThread);
            }
        }
    }

    public void stopDownLoadPiecesThread(boolean mStopThread) {
        if (mStopThread) {
            for (FilePiecesDownloadThread filePiecesDownloadThread : mFilePiecesDownloadThreads) {
                filePiecesDownloadThread.setmStopThread(true);
            }
        }
    }

    public int getDownLoadProgress(){
        long totalLen = 0;
        long tempLen = 0;

        for(FileInfo fileInfo : mfileInfos){
            totalLen += fileInfo.getEnd() - fileInfo.getStart() + 1;
            tempLen += fileInfo.getCursor();
        }
        Log.w(TAG, "getDownLoadProgress: url = " + mfileInfos.get(0).getUrl() + "; tempLen = " + tempLen + "; totalLen = " + totalLen + "; percent = " + (int)(tempLen  * 100 / totalLen) + "%");
        if (totalLen != 0){
            return (int)(tempLen  * 100 / totalLen);
        }else{
            return 0;
        }
    }

}
