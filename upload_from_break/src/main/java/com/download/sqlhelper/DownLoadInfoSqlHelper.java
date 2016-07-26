package com.download.sqlhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.download.constant.ConstantDatas;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class DownLoadInfoSqlHelper extends SQLiteOpenHelper{
    private static final  String CREATE_TABLE = "create table if not exists " + ConstantDatas.DOWNLOAD_FILEINFO_TABLE_NAME + "(_id integer primary key autoincrement, url text, threadid integer, start integer, end integer, cursor integer)";
    private static final  String DELETE_TABLE = "drop table if exists";
    public DownLoadInfoSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        db.execSQL(CREATE_TABLE);
    }
}
