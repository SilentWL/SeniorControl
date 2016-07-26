package com.seniorcontrol.administrator.progressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class CustomProgressBar extends View {
    int firstColor = 0;
    int secondColor = 0;
    int circleWidth = 0;
    int speed = 0;
    int mProgress = 0;
    boolean mFirstProgress = true;

    Paint mPaint = null;


    public CustomProgressBar(Context context) {
        this(context, null);


    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);

        int typeCount = typedArray.getIndexCount();

        for (int i = 0; i < typeCount; i++){
            int type = typedArray.getIndex(i);
            switch (type){
                case R.styleable.CustomProgressBar_firstColor:
                    firstColor = typedArray.getColor(type, Color.RED);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    secondColor = typedArray.getColor(type, Color.YELLOW);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    circleWidth = typedArray.getDimensionPixelSize(type, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_speed:
                    speed = typedArray.getInteger(type, 20);
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();

        new Thread(){
            @Override
            public void run() {
                //super.run();
                while (true){
                    ++mProgress;
                    if (mProgress >= 360){
                        mProgress = 0;
                        mFirstProgress = !mFirstProgress;
                    }
                    postInvalidate();

                    try {
                        Thread.sleep(speed);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int radius = centerX - circleWidth;

        mPaint.setStrokeWidth(circleWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        RectF oval = new RectF(centerX - radius, centerX - radius, centerX + radius, centerX + radius);

        if (mFirstProgress){
            mPaint.setColor(firstColor);
            canvas.drawCircle(centerX, centerX, radius, mPaint);
            mPaint.setColor(secondColor);
            canvas.drawArc(oval, 0, mProgress, false, mPaint);
        }
        else{
            mPaint.setColor(secondColor);
            canvas.drawCircle(centerX, centerX, radius, mPaint);
            mPaint.setColor(firstColor);
            canvas.drawArc(oval, 0, mProgress, false, mPaint);
        }

    }
}
