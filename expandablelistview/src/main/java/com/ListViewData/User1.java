package com.ListViewData;

import com.datahelper.AnnotationNodeId;
import com.datahelper.AnnotationNodePid;
import com.datahelper.AnnotationNodeTagString;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class User1 {
    @AnnotationNodeId
    int id;
    @AnnotationNodePid
    int pid;
    @AnnotationNodeTagString
    String tag;

    public User1(int id, int pid, String tag) {
        this.id = id;
        this.pid = pid;
        this.tag = tag;
    }
}
