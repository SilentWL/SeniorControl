package com.download.sqlhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.download.constant.ConstantDatas;
import com.download.utils.FileInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class SqlOperation implements SqlOperationInterface {
    private static SqlOperation mSqlOperation = null;
    private DownLoadInfoSqlHelper mDownLoadInfoSqlHelper = null;
    private Context mContext = null;

    private SqlOperation(){
    }
    private SqlOperation(Context context){
        if (mDownLoadInfoSqlHelper == null) {
            mContext = context;
            mDownLoadInfoSqlHelper = new DownLoadInfoSqlHelper(context, ConstantDatas.DOWNLOAD_FILEINFO_DATABASE_NAME, null, ConstantDatas.DOWNLOAD_FILEINFO_TABLE_VERSION);
        }
    }
    public static SqlOperation getInstance(Context context){
        if (mSqlOperation == null){
            synchronized (SqlOperation.class){
                    mSqlOperation = new SqlOperation(context);

                }
            }
            if (mSqlOperation == null){
        }
        return mSqlOperation;
    }

    @Override
    public synchronized void writeFileInfoToDBByThreadId(FileInfo fileInfo) {
        if (mDownLoadInfoSqlHelper != null && fileInfo != null) {
            SQLiteDatabase db = mDownLoadInfoSqlHelper.getWritableDatabase();

            db.execSQL("update " + ConstantDatas.DOWNLOAD_FILEINFO_TABLE_NAME + " set start = ?, end = ?, cursor = ? where url = ? and threadid = ?", new Object[]{fileInfo.getStart(), fileInfo.getEnd(), fileInfo.getCursor(), fileInfo.getUrl(), fileInfo.getThreadId()});
            db.close();
        }
    }
    @Override
    public synchronized void writeFileInfoThreadIdToDB(FileInfo fileInfo) {
        if (mDownLoadInfoSqlHelper != null && fileInfo != null) {
            SQLiteDatabase db = mDownLoadInfoSqlHelper.getWritableDatabase();

            db.execSQL("update " + ConstantDatas.DOWNLOAD_FILEINFO_TABLE_NAME + " set threadid = ? where url = ? and start = ? and end = ? and cursor = ? ", new Object[]{fileInfo.getThreadId(), fileInfo.getUrl(), fileInfo.getStart(), fileInfo.getEnd(), fileInfo.getCursor()});
            db.close();
        }
    }
    @Override
    public synchronized void  insertFileInfosToDB(List<FileInfo> fileInfos) {
        if (mDownLoadInfoSqlHelper != null && fileInfos.size() > 0){
            SQLiteDatabase db = mDownLoadInfoSqlHelper.getWritableDatabase();
            for (FileInfo fileInfo : fileInfos){
                db.execSQL("insert into " + ConstantDatas.DOWNLOAD_FILEINFO_TABLE_NAME + "(url, threadid, start, end, cursor)" + " values(?, ?, ?, ?, ?)", new Object[]{fileInfo.getUrl(), fileInfo.getThreadId(), fileInfo.getStart(), fileInfo.getEnd(), + fileInfo.getCursor()});
            }
            db.close();
        }
    }
    @Override
    public synchronized void insertFileInfoToDB(FileInfo fileInfo) {
        if (mDownLoadInfoSqlHelper != null && fileInfo != null){
            SQLiteDatabase db = mDownLoadInfoSqlHelper.getWritableDatabase();
            db.execSQL("insert into " + ConstantDatas.DOWNLOAD_FILEINFO_TABLE_NAME + "(url, threadid, start, end, cursor)" + " values(?, ?, ?, ?, ?)", new Object[]{fileInfo.getUrl(), fileInfo.getThreadId(), fileInfo.getStart(), fileInfo.getEnd(), + fileInfo.getCursor()});
            db.close();
        }
    }
    @Override
    public synchronized void deleteFileInfoFromDB(FileInfo fileInfo) {
        if (mDownLoadInfoSqlHelper != null && fileInfo != null) {
            SQLiteDatabase db = mDownLoadInfoSqlHelper.getWritableDatabase();
            db.execSQL("delete from " + ConstantDatas.DOWNLOAD_FILEINFO_TABLE_NAME + " where url = ? and threadid = ? and start = ? and end = ? and cursor = ?", new Object[]{fileInfo.getUrl(), fileInfo.getThreadId(), fileInfo.getStart(), fileInfo.getEnd(), fileInfo.getCursor()});
            db.close();
        }

    }

    @Override
    public synchronized void deleteFileInfoFromDB(String url) {
        if (mDownLoadInfoSqlHelper != null && url != null) {
            SQLiteDatabase db = mDownLoadInfoSqlHelper.getWritableDatabase();
            db.execSQL("delete from " + ConstantDatas.DOWNLOAD_FILEINFO_TABLE_NAME + " where url = ?", new Object[]{url});
            db.close();
        }
    }

    @Override
    public synchronized void readFileInfosFromDB(String url, List<FileInfo> infileInfos) {
        if (mDownLoadInfoSqlHelper != null && infileInfos != null) {
            SQLiteDatabase db = mDownLoadInfoSqlHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + ConstantDatas.DOWNLOAD_FILEINFO_TABLE_NAME + " where url = ?", new String[]{url});

            while (cursor.moveToNext()){
                FileInfo fileInfo = new FileInfo(cursor.getString(cursor.getColumnIndex("url")), cursor.getInt(cursor.getColumnIndex("cursor")), cursor.getInt(cursor.getColumnIndex("start")), cursor.getInt(cursor.getColumnIndex("end")), cursor.getInt(cursor.getColumnIndex("threadid")));
                infileInfos.add(fileInfo);
            }
            db.close();
        }
    }
}
