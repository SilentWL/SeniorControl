package com.download.constant;

import android.os.Environment;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class ConstantDatas {
    public static final String DOWNLOAD_DIR = Environment.getExternalStorageDirectory() + "/WLDownload";

    public static final String START_DOWNLOAD_INTENT_KEY_URL = "START_DOWNLOAD_INTENT_KEY_URL";

    public static final int DOWNLOAD_SERVICE_GET_FILEINFOS_SUCCESS_MSG = 100;
    public static final int DOWNLOAD_SERVICE_START_DOWNLOAD_MSG = 101;
    public static final int DOWNLOAD_SERVICE_DOWNLOADING_MSG = 102;
    public static final int DOWNLOAD_SERVICE_FILE_PIECE_FINISH_MSG = 103;


    public static final int DOWNLOAD_SERVICE_FILE_PIECES_COUNT = 3;

    public static final String DOWNLOAD_SERVICE_START_DOWNLOAD = "DOWNLOAD_SERVICE_START_DOWNLOAD";
    public static final String DOWNLOAD_SERVICE_STOP_DOWNLOAD = "DOWNLOAD_SERVICE_STOP_DOWNLOAD";

    public static final String DOWNLOAD_FILEINFO_DATABASE_NAME = "FileInfo.db";
    public static final String DOWNLOAD_FILEINFO_TABLE_NAME = "FileInfos";
    public static final int DOWNLOAD_FILEINFO_TABLE_VERSION = 1;

    public static final String UI_UPDATE_PROGRESS_BROADCAST_ACTION = "UI_UPDATE_PROGRESS_BROADCAST_ACTION";
    public static final String UI_UPDATE_PROGRESS_DATA = "UI_UPDATE_PROGRESS_DATA";
    public static final String UI_UPDATE_PROGRESS_KEY_URL = "UI_UPDATE_PROGRESS_KEY_URL";
}
