package com.seniorcontrol.administrator.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class HorizontalProgressBar extends ProgressBar{
    private static final int DEFAULT_TEXT_SIZE = 10;    //sp
    private static final int DEFAULT_TEXT_COLOR = 0Xfffc00d1;
    private static final int DEFAULT_COLOR_UNREACH = 0XFFD3D6DA;
    private static final int DEFAULT_WIDTH_UNREACH = 2;   //dp
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_WIDTH_REACH = 2;     //dp
    private static final int DEFAULT_TEXT_OFFSET = 10;   //dp

    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mUnreachColor = DEFAULT_COLOR_UNREACH;
    private int mUnreachWidth = dp2px(DEFAULT_WIDTH_UNREACH);
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mReachWidth = dp2px(DEFAULT_WIDTH_REACH);
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    private Paint mPaint = new Paint();
    private int mRealWidth = 0;

    private RefreshData refreshData = null;

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar, defStyleAttr, 0);

        int typeCount = typedArray.getIndexCount();

        for (int i = 0; i < typeCount; i++){
            int typeIndex = typedArray.getIndex(i);

            switch (typeIndex){
                case R.styleable.HorizontalProgressBar_progress_reach_color:
                    mReachColor = typedArray.getColor(typeIndex, mReachColor);
                    break;
                case R.styleable.HorizontalProgressBar_progress_reach_width:
                    mReachWidth = typedArray.getDimensionPixelSize(typeIndex, mReachWidth);
                    break;
                case R.styleable.HorizontalProgressBar_progress_text_color:
                    mTextColor = typedArray.getColor(typeIndex, mTextColor);
                    break;
                case R.styleable.HorizontalProgressBar_progress_text_offset:
                    mTextOffset = typedArray.getDimensionPixelSize(typeIndex, mTextOffset);
                    break;
                case R.styleable.HorizontalProgressBar_progress_text_size:
                    mTextSize = typedArray.getDimensionPixelSize(typeIndex, mTextSize);
                    break;
                case R.styleable.HorizontalProgressBar_progress_unreach_color:
                    mUnreachColor = typedArray.getColor(typeIndex, mUnreachColor);
                    break;
                case R.styleable.HorizontalProgressBar_progress_unreach_width:
                    mUnreachWidth = typedArray.getDimensionPixelSize(typeIndex, mUnreachWidth);
                    break;
            }

        }
        typedArray.recycle();
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int desireWidth = 0;
        int desireHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY){
            desireWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY){
            desireHeight = heightSize;

        }else{
            int textHeight = (int)Math.ceil(mPaint.descent() - mPaint.ascent());

            desireHeight = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mReachWidth, mUnreachWidth), textHeight);

            if (heightMode == MeasureSpec.AT_MOST){
                desireHeight = Math.min(desireHeight, heightSize);
            }
        }

        setMeasuredDimension(desireWidth, desireHeight);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight()/2);

        String text = "100" + "%";
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int textWidth = rect.right - rect.left;

        float progressPercent = (float) (getProgress() * 1.0/ getMax());
        float progressWidth = mRealWidth - 2 * mTextOffset - textWidth;
        float reachWidth = progressPercent * progressWidth ;
        float unreachWidth = mRealWidth - reachWidth - 2 * mTextOffset - textWidth;


        if (reachWidth > 0){
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachWidth);
            canvas.drawLine(0, 0, reachWidth, 0, mPaint);
        }

        mPaint.setColor(mTextColor);
        text = getProgress() + "%";
        mPaint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, reachWidth + mTextOffset + (textWidth - (rect.right - rect.left)) / 2, (rect.bottom - rect.top) / 2, mPaint);

        if (unreachWidth > 0){
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachWidth);
            canvas.drawLine(reachWidth + mTextOffset * 2 + textWidth, 0, unreachWidth + reachWidth + mTextOffset * 2 + textWidth, 0, mPaint);
        }
        canvas.restore();
    }

    public void SetRefreshDateInterface(RefreshData refreshData){
        this.refreshData = refreshData;
    }
    public interface RefreshData{
        public void onRefreshData();
    }
}
