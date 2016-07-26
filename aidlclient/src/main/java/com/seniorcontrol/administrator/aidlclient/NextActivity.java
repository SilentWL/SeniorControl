package com.seniorcontrol.administrator.aidlclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class NextActivity extends Activity{
    public static final String ACTION_NEXT = "com.seniorcontrol.administrator.aidlclient.NEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);
        Intent intent = getIntent();
        intent.getAction();
    }

    private class SurfaceTest extends SurfaceView implements SurfaceHolder.Callback{

        public SurfaceTest(Context context) {
            super(context);
        }

        public SurfaceTest(Context context, AttributeSet attrs) {
            super(context, attrs);
            getHolder().addCallback(this);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

}
