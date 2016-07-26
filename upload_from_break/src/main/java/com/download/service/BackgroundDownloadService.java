package com.download.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.download.constant.ConstantDatas;
import com.download.sqlhelper.SqlOperation;
import com.download.utils.FileInfo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class BackgroundDownloadService extends Service{
    private boolean mPauseDownload = true;
    private Handler mProcessServiceHandler = null;
    private Context mContext = null;
    private static final String TAG = "BackgroundDownload";

    private Map<String, FileDownloadTask> mDownLoadTaskMap = new HashMap<String, FileDownloadTask>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mProcessServiceHandler == null){
            mProcessServiceHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);

                    if (msg.what == ConstantDatas.DOWNLOAD_SERVICE_GET_FILEINFOS_SUCCESS_MSG){
                        List<FileInfo> fileInfos = (List<FileInfo>) msg.obj;
                        SqlOperation.getInstance(mContext).insertFileInfosToDB(fileInfos);
                        mProcessServiceHandler.obtainMessage(ConstantDatas.DOWNLOAD_SERVICE_START_DOWNLOAD_MSG, fileInfos).sendToTarget();
                    }else if (msg.what == ConstantDatas.DOWNLOAD_SERVICE_START_DOWNLOAD_MSG){
                        List<FileInfo> fileInfos = (List<FileInfo>) msg.obj;

                        startDownLoadFile(fileInfos);
                    }else if (msg.what == ConstantDatas.DOWNLOAD_SERVICE_DOWNLOADING_MSG){
                        FileInfo fileInfo = (FileInfo) msg.obj;
                        Log.w(TAG, "Threadid = " +  fileInfo.getThreadId());

                        sendProgressBroadcastToUI(fileInfo, mContext);
                    }else if (msg.what == ConstantDatas.DOWNLOAD_SERVICE_FILE_PIECE_FINISH_MSG){
                        FileInfo fileInfo = (FileInfo) msg.obj;
                        List<FileInfo> fileInfos = getFileInfosFromDB(mContext, fileInfo.getUrl());
                        sendProgressBroadcastToUI(fileInfo, mContext);
                        if (fileInfos.size() > 0){
                            if (calcProgress(fileInfos) == 100) {
                                removeUnusedFileInfosFromDB(mContext, fileInfos);
                                String url = fileInfos.get(0).getUrl();
                                if (mDownLoadTaskMap.containsKey(url)){
                                    mDownLoadTaskMap.get(url).stopDownLoadPiecesThread(true);
                                    mDownLoadTaskMap.remove(url);
                                }
                            }
                        }
                        Log.w(TAG, "DOWNLOAD_SERVICE_FILE_PIECE_FINISH_MSG");
                    }
                }
            };
        }
        if (mContext == null){
            mContext = getApplicationContext();
        }
        String url = intent.getStringExtra(ConstantDatas.START_DOWNLOAD_INTENT_KEY_URL);

        List<FileInfo> fileInfos = getFileInfosFromDB(mContext, url);
        if (fileInfos.size() > 0 && calcProgress(fileInfos) == 100){
            removeUnusedFileInfosFromDB(mContext, fileInfos);
        }
        if (intent.getAction().equals(ConstantDatas.DOWNLOAD_SERVICE_START_DOWNLOAD)){
            if (!mDownLoadTaskMap.containsKey(url)) {
                if (fileInfos.size() != 0) {
                    mProcessServiceHandler.obtainMessage(ConstantDatas.DOWNLOAD_SERVICE_START_DOWNLOAD_MSG, fileInfos).sendToTarget();
                } else {
                    getFileInfos(url);
                }
            }

        }else if (intent.getAction().equals(ConstantDatas.DOWNLOAD_SERVICE_STOP_DOWNLOAD)){
            if (mDownLoadTaskMap.containsKey(url)){
                mDownLoadTaskMap.get(url).stopDownLoadPiecesThread(true);
                mDownLoadTaskMap.remove(url);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
    public static int getDownloadProgress(Context mContext, String url) {
        long totalLen = 0;
        long tempLen = 0;

        List<FileInfo> fileInfos = new ArrayList<FileInfo>();

        SqlOperation.getInstance(mContext).readFileInfosFromDB(url, fileInfos);

        for(FileInfo fileInfo : fileInfos){
            totalLen += fileInfo.getEnd() - fileInfo.getStart() + 1;
            tempLen += fileInfo.getCursor();
        }

        if (totalLen != 0){
            return (int)(tempLen  * 100 / totalLen);
        }else{
            return 0;
        }
    }
    private void sendProgressBroadcastToUI(FileInfo fileInfo, Context context){
        Intent intent = new Intent();
        intent.setAction(ConstantDatas.UI_UPDATE_PROGRESS_BROADCAST_ACTION);
        intent.putExtra(ConstantDatas.UI_UPDATE_PROGRESS_KEY_URL, fileInfo.getUrl());
        if (mDownLoadTaskMap.containsKey(fileInfo.getUrl())){
            intent.putExtra(ConstantDatas.UI_UPDATE_PROGRESS_DATA, mDownLoadTaskMap.get(fileInfo.getUrl()).getDownLoadProgress());
        }else {
            intent.putExtra(ConstantDatas.UI_UPDATE_PROGRESS_DATA, getDownloadProgress(context, fileInfo.getUrl()));
        }
        sendBroadcast(intent);
    }
    private int calcProgress(List<FileInfo> fileInfos) {
        long totalLen = 0;
        long tempLen = 0;
        for(FileInfo fileInfo : fileInfos){
            totalLen += fileInfo.getEnd() - fileInfo.getStart() + 1;
            tempLen += fileInfo.getCursor();
        }
        if (totalLen != 0){
            return (int)(tempLen  * 100 / totalLen);
        }else{
            return 0;
        }
    }

    private List<FileInfo> getFileInfosFromDB(Context mContext, String url) {
        List<FileInfo> fileInfos = new ArrayList<FileInfo>();

        SqlOperation.getInstance(mContext).readFileInfosFromDB(url, fileInfos);

        return fileInfos;
    }

    private void removeUnusedFileInfosFromDB(Context mContext, List<FileInfo> fileInfos) {
        for (FileInfo fileInfo : fileInfos) {
            if (fileInfo.getEnd() - fileInfo.getStart() + 1 == fileInfo.getCursor()) {
                SqlOperation.getInstance(mContext).deleteFileInfoFromDB(fileInfo);
            }
        }
    }

    private void getFileInfos(final String stringExtra) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                HttpURLConnection conn = null;

                try {
                    url = new URL(stringExtra);
                    conn = (HttpURLConnection) url.openConnection();

                    int fileLength = -1;

                    if (conn != null) {
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);

                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            fileLength = conn.getContentLength();
                        }

                        if (fileLength >= 0) {
                            List<FileInfo> fileInfos = new ArrayList<FileInfo>();

                            int fileOnePieceSize = (int)Math.ceil((double) fileLength / ConstantDatas.DOWNLOAD_SERVICE_FILE_PIECES_COUNT);

                            for (int i = 0; i < ConstantDatas.DOWNLOAD_SERVICE_FILE_PIECES_COUNT; i++) {
                                FileInfo fileInfo = new FileInfo(stringExtra, 0, i * fileOnePieceSize, (i + 1) * fileOnePieceSize - 1, -1);
                                fileInfos.add(fileInfo);
                            }
                            fileInfos.get(fileInfos.size() - 1).setEnd(fileLength - 1);

                            mProcessServiceHandler.obtainMessage(ConstantDatas.DOWNLOAD_SERVICE_GET_FILEINFOS_SUCCESS_MSG, fileInfos).sendToTarget();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startDownLoadFile(List<FileInfo> fileInfos) {
        if (fileInfos.size() > 0) {

            FileDownloadTask fileDownloadTask = new FileDownloadTask(fileInfos, mContext, mProcessServiceHandler);
            mDownLoadTaskMap.put(fileInfos.get(0).getUrl(), fileDownloadTask);

            fileDownloadTask.downloadFile();
        }
    }
}
