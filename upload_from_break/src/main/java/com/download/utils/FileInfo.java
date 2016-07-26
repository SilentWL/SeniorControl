package com.download.utils;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class FileInfo {
    private String url;
    private long start;
    private long end;
    private long cursor;
    private long threadId;

    public FileInfo(String url, long cursor, long start, long end, int threadId) {
        this.cursor = cursor;
        this.end = end;
        this.start = start;
        this.threadId = threadId;
        this.url = url;
    }

    public long getCursor() {
        return cursor;
    }

    public void setCursor(long cursor) {
        this.cursor = cursor;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
