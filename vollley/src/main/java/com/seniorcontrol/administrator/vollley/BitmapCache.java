package com.seniorcontrol.administrator.vollley;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class BitmapCache implements ImageLoader.ImageCache{
    private LruCache<String, Bitmap> mCache;
    private long mMaxCacheMemory = 10 * 1024 * 1024;

    public BitmapCache(Context context) {
        //ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        //((ActivityManager)context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        //mMaxCacheMemory = memoryInfo.availMem / 5;
        mMaxCacheMemory = Runtime.getRuntime().maxMemory() / 5;
        mCache = new LruCache<String, Bitmap>((int)mMaxCacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }


    @Override
    public Bitmap getBitmap(String s) {
        return mCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        mCache.put(s, bitmap);

    }
}
