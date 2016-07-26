package com.seniorcontrol.administrator.vollley;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public abstract class VolleyInterface {
    public Context mContext;
    private Response.Listener<String> mListener;
    private Response.ErrorListener mErrorListener;

    public VolleyInterface(Context mContext, Response.ErrorListener mErrorListener, Response.Listener<String> mListener) {
        this.mContext = mContext;
        this.mErrorListener = mErrorListener;
        this.mListener = mListener;
    }
    public abstract void onSuccess(String s);
    public abstract void onError(VolleyError volleyError);

    public Response.Listener loadingListener(){
        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                onSuccess(s);
            }
        };
        return  mListener;
    }
    public Response.ErrorListener loadingErrorListener(){
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onError(volleyError);
            }
        };
        return  mErrorListener;
    }
}
