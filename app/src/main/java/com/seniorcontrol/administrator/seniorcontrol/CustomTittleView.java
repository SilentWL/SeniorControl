package com.seniorcontrol.administrator.seniorcontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class CustomTittleView extends View implements View.OnClickListener{
    private String mTittleText = null;
    private int mTittleTextColor = 0;
    private int mTittleTextSize = 0;

    private Rect mBound = null;
    private Paint mPaint = null;

    public CustomTittleView(Context context) {
        this(context, null);
    }

    public CustomTittleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTittleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTittleView, defStyleAttr, 0);

        int n = typedArray.getIndexCount();

        for (int i = 0; i < n; i++){
            int attr = typedArray.getIndex(i);

            switch (attr){
                case R.styleable.CustomTittleView_tittleText:
                    mTittleText = typedArray.getString(attr);
                    break;
                case R.styleable.CustomTittleView_tittleTextColor:
                    mTittleTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTittleView_tittleTextSize:
                    mTittleTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTittleTextSize);

        mBound = new Rect();
        mPaint.getTextBounds(mTittleText, 0, mTittleText.length(), mBound);

        /*
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();

                Set<Integer> set = new HashSet<Integer>();

                while (set.size() < 4){
                    int randomInt = random.nextInt(10);
                    set.add(randomInt);
                }

                StringBuffer sb = new StringBuffer();

                for (Integer i:set){
                    sb.append("" + i);
                }

                mTittleText = sb.toString();

                postInvalidate();
            }
        });*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTittleTextColor);
        canvas.drawText(mTittleText, (getWidth() - mBound.width())/2, (getHeight() + mBound.height())/2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;

        }else{
            mPaint.setTextSize(mTittleTextSize);
            mPaint.getTextBounds(mTittleText, 0, mTittleText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int)(getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;

        }else{
            mPaint.setTextSize(mTittleTextSize);
            mPaint.getTextBounds(mTittleText, 0, mTittleText.length(), mBound);
            height = (int)(getPaddingTop() + mBound.height() + getPaddingBottom());
        }

        setMeasuredDimension(width, height);
    }




    @Override
    public void onClick(View v) {
        Random random = new Random();

        Set<Integer> set = new HashSet<Integer>();

        while (set.size() < 4){
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }

        StringBuffer sb = new StringBuffer();

        for (Integer i:set){
            sb.append("" + i);
        }

        mTittleText = sb.toString();

        postInvalidate();
    }
}
