package com.seniorcontrol.administrator.volumechangecontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/6/17 0017.
 */
public class VolumeChangeControl extends View implements View.OnTouchListener{
    int mCircleWidth = 0;
    int mDefaultVolume = 0;
    int mFirstColor = 0;
    int mSecondColor = 0;
    Bitmap mBitmap = null;
    int mVolumeStep = 0;
    Paint mPaint = null;


    int mCurrentVolume = 0;
    int mStepDegrees = 0;
    int mBlankDegrees = 0;
    RectF mRectF = null;
    Rect mBitmapRect = null;

    int mDown = 0;
    int mUp = 0;
    static final int STEP_DEGREES = 8;
    static final int STEP_BLANK_DEGREES = 15;
    public VolumeChangeControl(Context context) {
        this(context, null);
    }

    public VolumeChangeControl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeChangeControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VolumeChangeControl, defStyleAttr, 0);

        int typeCount = typedArray.getIndexCount();

        for (int i = 0; i < typeCount; i++){
            int index = typedArray.getIndex(i);

            switch (index){
                case R.styleable.VolumeChangeControl_circleWidth:
                    mCircleWidth = typedArray.getDimensionPixelSize(index, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.VolumeChangeControl_defaultVolume:
                    mCurrentVolume = mDefaultVolume = typedArray.getInteger(index, 6);
                    break;
                case R.styleable.VolumeChangeControl_firstColor:
                    mFirstColor = typedArray.getColor(index, Color.GRAY);
                    break;
                case R.styleable.VolumeChangeControl_secondColor:
                    mSecondColor = typedArray.getColor(index, Color.WHITE);
                    break;
                case R.styleable.VolumeChangeControl_image:
                    mBitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(index, 0));
                    break;
                case R.styleable.VolumeChangeControl_volumeStep:
                    mVolumeStep = typedArray.getInteger(index, 16);
                    break;
                case R.styleable.VolumeChangeControl_stepDegrees:
                    mStepDegrees = typedArray.getInteger(index, STEP_DEGREES);
                    break;
                case R.styleable.VolumeChangeControl_stepBlankDegrees:
                    mBlankDegrees = typedArray.getInteger(index, STEP_BLANK_DEGREES);
                    break;


            }
        }

        typedArray.recycle();

        mPaint = new Paint();

        mBitmapRect = new Rect();

        mRectF = new RectF();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDown = (int)event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mUp = (int)event.getY();

                if (mUp > mDown){
                    if (mCurrentVolume >= 1){
                        mCurrentVolume--;
                        postInvalidate();
                    }

                }else {
                    if (mCurrentVolume < mVolumeStep) {
                        mCurrentVolume++;
                        postInvalidate();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mFirstColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        int centerX = getWidth() / 2;
        int radius = centerX - mCircleWidth/2;

        mRectF.set(centerX - radius, centerX - radius, centerX + radius, centerX + radius);
        DrawStep(true, mVolumeStep, mVolumeStep, mRectF, mPaint, canvas);
        mPaint.setColor(mSecondColor);
        DrawStep(false, mCurrentVolume, mVolumeStep, mRectF, mPaint, canvas);

        int internalRadius = radius - mCircleWidth / 2;
        mBitmapRect.left = (int)(radius - internalRadius * Math.sqrt(2) / 2 + mCircleWidth);
        mBitmapRect.top = mBitmapRect.left;
        mBitmapRect.right = centerX * 2 - mBitmapRect.left;
        mBitmapRect.bottom = mBitmapRect.right;


        if (mBitmap.getWidth() < Math.sqrt(2) * internalRadius){

            mBitmapRect.left =  centerX - mBitmap.getWidth()/2;
            mBitmapRect.top = centerX - mBitmap.getHeight()/2;
            mBitmapRect.right = centerX + mBitmap.getWidth() / 2 ;
            mBitmapRect.bottom = centerX + mBitmap.getHeight() / 2;
        }
        canvas.drawBitmap(mBitmap, null, mBitmapRect, mPaint);


    }

    private void DrawStep(Boolean fromLeft, int drawVolumeCount, int maxVolume, RectF drawRect, Paint paint, Canvas canvas){
        float totalDegrees = mStepDegrees * maxVolume + mBlankDegrees * (maxVolume - 1);
        float drawDegrees = mStepDegrees * drawVolumeCount + mBlankDegrees * (drawVolumeCount - 1);


        if (totalDegrees > 360){
            totalDegrees = 360;
        }

        if (drawDegrees > 360){
            drawDegrees = 360;
        }

        float startDegrees = (1 - (float)totalDegrees / 360) * 360;
        if (totalDegrees > 180) {
            startDegrees += ((float) totalDegrees - 180) / 2;
        } else {
            startDegrees = 180 + (startDegrees - 180) / 2;
        }
        if (!fromLeft) {
            startDegrees+=totalDegrees - drawDegrees;

        }
        for (int i = 0; i < drawVolumeCount; i++) {
            canvas.drawArc(drawRect, startDegrees, mStepDegrees, false, paint);
            startDegrees += mStepDegrees + mBlankDegrees;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int desireWidth = MeasureSpec.getSize(widthMeasureSpec);
        int desireHeight = desireWidth;

        if (widthMode != MeasureSpec.EXACTLY){
            desireHeight = desireWidth = (int)(Math.max(mBitmap.getWidth(), mBitmap.getHeight()) * Math.sqrt(2)) + mCircleWidth;
        }
        setMeasuredDimension(desireWidth, desireHeight);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }
}
