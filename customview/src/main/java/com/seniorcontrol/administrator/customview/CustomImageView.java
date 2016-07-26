package com.seniorcontrol.administrator.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class CustomImageView extends View {
    Bitmap  mImage = null;
    int mImageScale = 0;
    String mTitle = null;
    int mTextColor = 0;
    int mTextSize = 0;
    Rect mRect = null;
    Paint mPaint = null;
    Rect mTextBound = null;
    int mWidth = 0;
    int mHeight = 0;
    static final int IMAGE_SCALE_FITXY = 0;
    static final int IMAGE_SCALE_CENTER = 0;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        //TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);

        int count = typedArray.getIndexCount();

        for (int i = 0; i < count; i++){
            int attr = typedArray.getIndex(i);

            switch (attr){
                case R.styleable.CustomImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = typedArray.getInt(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleText:
                    mTitle=typedArray.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor0:
                    mTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    mTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }

        typedArray.recycle();
        mRect = new Rect();
        mPaint = new Paint();
        mTextBound = new Rect();
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mTextColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        if (mTextBound.width() > mWidth){
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitle, paint, (float)mWidth - getPaddingLeft() - getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        }
        else
        {
            canvas.drawText(mTitle, mWidth/2 - mTextBound.width()*1.0f / 2, mHeight-getPaddingBottom(), mPaint);
        }

        mRect.bottom -= mTextBound.height();

        if (mImageScale == IMAGE_SCALE_FITXY){
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }else {
            mRect.left = mWidth/2 - mImage.getWidth()/2;
            mRect.right = mWidth/2 + mImage.getWidth()/2;
            mRect.top = (mHeight - mTextBound.height())/2 - mImage.getHeight()/2;
            mRect.bottom = (mHeight - mTextBound.height())/2 + mImage.getHeight()/2;
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){
            mWidth = specSize;
        }else{
            int desireImage = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            int desireTitle = getPaddingLeft() + getPaddingLeft() + mTextBound.width();

            if (specMode == MeasureSpec.AT_MOST){
                int desire = Math.max(desireImage, desireTitle);
                mWidth = Math.min(desire, specSize);
            }
        }

        specSize = MeasureSpec.getSize(heightMeasureSpec);
        specMode = MeasureSpec.getMode(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){
            mHeight = specSize;
        }else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mTextBound.height();
            if (specMode == MeasureSpec.AT_MOST){
                mHeight = Math.min(desire, specSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }
}
