package com.download.sqlhelper;

import com.download.utils.FileInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public interface SqlOperationInterface {
    void insertFileInfosToDB(List<FileInfo> fileInfos);
    void deleteFileInfoFromDB(FileInfo fileInfo);
    void deleteFileInfoFromDB(String url);
    void readFileInfosFromDB(String url, List<FileInfo> fileInfos);
    void insertFileInfoToDB(FileInfo fileInfo);

    void writeFileInfoToDBByThreadId(FileInfo fileInfo);
    void writeFileInfoThreadIdToDB(FileInfo fileInfo);

}
