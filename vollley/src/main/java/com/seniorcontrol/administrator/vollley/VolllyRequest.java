package com.seniorcontrol.administrator.vollley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class VolllyRequest {
    public static StringRequest mStringRequest;
    public static Context mContext;

    public static void RequestGet(Context context, String url, String tag, VolleyInterface volleyInterface){
        MyApplication.getHttpQueues().cancelAll(tag);

        mStringRequest = new StringRequest(Request.Method.GET, url, volleyInterface.loadingListener(), volleyInterface.loadingErrorListener());
        mStringRequest.setTag(tag);
        MyApplication.getHttpQueues().add(mStringRequest);
        MyApplication.getHttpQueues().start();
    }

    public static void RequestPost(Context context, String url, String tag, Map<String, String> map, VolleyInterface volleyInterface){

    }
}
