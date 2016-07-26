package com.seniorcontrol.administrator.scrathcard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class ScrathCardView extends View{
    private final int SCRATH_CARD_FORECOLOR = 0x3e3b3b;
    private final int SCRATH_CARD_SHADOWLINECOLOR = 0xbfcfc8c8;
    private final String SCRATH_CARD_BACKTEXT = "Thanks!";
    private final int SCRATH_CARD_BACKTEXTSIZE = 15; //sp
    private final int SCRATH_CARD_BACKTEXTCOLOR = 0xa5c7132e;
    private final int SCRATH_CARD_BACKIMAGEID = R.mipmap.ic_launcher;
    private final int SCRATH_CARD_SHADOWLINECOUNT = 20;
    private final int SCRATH_CARD_WIDTH = 300;
    private final int SCRATH_CARD_HEIGHT = 200;
    private final int SCRATH_CARD_SHADOWLINEWIDTH = 5; //dp

    private int mShadowLineWidth = dp2Px(SCRATH_CARD_SHADOWLINEWIDTH);
    private int mBackTextColor = SCRATH_CARD_BACKTEXTCOLOR;
    private int mBackTextSize = sp2Px(SCRATH_CARD_BACKTEXTSIZE);
    private String mBackText = SCRATH_CARD_BACKTEXT;
    private int mForeColor = SCRATH_CARD_FORECOLOR;
    private int mShadowLineColor = SCRATH_CARD_SHADOWLINECOLOR;
    private Bitmap mBackImageBitmap = null;
    private Bitmap mForeImageBitmap = null;
    private int mShawLineCount = SCRATH_CARD_SHADOWLINECOUNT;

    private Canvas mCanvas = null;
    private Paint mPaint = null;
    private Path mPath = new Path();

    private int mWidth = 0;
    private int mHeight = 0;
    private boolean mShadowLineExist = false;


    private int sp2Px(int sp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private int dp2Px(int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    public ScrathCardView(Context context) {
        this(context, null);
    }

    public ScrathCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrathCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initTypeProperty(context, attrs, defStyleAttr);

    }

    public Bitmap drawForeImage(int width, int height) {
        if (mCanvas == null) {
            mCanvas = new Canvas(createBitmapFormForeColor(width, height));
            //mCanvas = new Canvas(mForeImageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true));
        }
        mCanvas.translate(getPaddingLeft(), getPaddingTop());

        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setAntiAlias(true);

        if (!mShadowLineExist) {
            drawShawLineOnCanvas(mCanvas, width, height);
            mShadowLineExist = true;
        }

        drawPath();
        return mForeImageBitmap;
    }

    private void drawPath() {
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(30);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mCanvas.drawPath(mPath, mPaint);
    }

    private void drawShawLineOnCanvas(Canvas canvas, int width, int height) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mShadowLineWidth);
        mPaint.setColor(mShadowLineColor);

        int shawLineGap = (width - mShadowLineWidth) / (mShawLineCount - 1);

        for(int i = 0; i < mShawLineCount; i++){
            canvas.drawLine(shawLineGap * (i + 1), 0, shawLineGap * i, height, mPaint);
        }
    }

    private Bitmap createBitmapFormForeColor(int width, int height) {
        int[] pix = new int[width * height];

        if (mForeImageBitmap != null) {
            return mForeImageBitmap;
        }
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
            {
                int index = y * width + x;
                //int r = ((pix[index] >> 16) & 0xff)|0xff;
                //int g = ((pix[index] >> 8) & 0xff)|0xff;
                //int b =( pix[index] & 0xff)|0xff;
                // pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
                pix[index] = mForeColor;

            }
        mForeImageBitmap = Bitmap.createBitmap(pix, width, height, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);
        return mForeImageBitmap;
    }

    private void initTypeProperty(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScrathCardView, defStyleAttr, 0);

        mBackTextColor = typedArray.getColor(R.styleable.ScrathCardView_BackTextColor, mBackTextColor);
        mBackTextSize = typedArray.getDimensionPixelSize(R.styleable.ScrathCardView_BackTextSize, mBackTextSize);
        mBackText = typedArray.getString(R.styleable.ScrathCardView_BackText);
        mForeColor = typedArray.getColor(R.styleable.ScrathCardView_ForeColor, mForeColor);
        mShadowLineColor = typedArray.getColor(R.styleable.ScrathCardView_ShadowLineColor, mShadowLineColor);
        mBackImageBitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.ScrathCardView_BackImageId, SCRATH_CARD_BACKIMAGEID));
        mShawLineCount = typedArray.getInteger(R.styleable.ScrathCardView_ShadowLineCount, mShawLineCount);
        mShadowLineWidth = typedArray.getDimensionPixelSize(R.styleable.ScrathCardView_ShadowLineWidth, mShadowLineWidth);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }else {
            mWidth = SCRATH_CARD_WIDTH;
        }
        if (heightMode == MeasureSpec.EXACTLY){
            mHeight = heightSize;
        }else{
            mHeight = SCRATH_CARD_HEIGHT;
        }

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.save();
        drawForeImage(mWidth - getPaddingLeft() - getPaddingRight(), mHeight - getPaddingTop() - getPaddingBottom());
        //mPaint.reset();
        Rect rect = new Rect(getPaddingLeft(), getPaddingTop(), mWidth - getPaddingRight(), mHeight - getPaddingBottom());

        canvas.drawBitmap(mBackImageBitmap, null, rect, null);

        canvas.drawBitmap(mForeImageBitmap, null, rect, null);
        canvas.restore();


    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle = (Bundle)state;
            mForeImageBitmap = (Bitmap) bundle.getParcelable(VIEW_BITMAP);
            mShadowLineExist = bundle.getBoolean(VIEW_SHADOW_EXIST);
            super.onRestoreInstanceState(bundle.getParcelable(VIEW_SRC_DATA));
            return;

        }
        super.onRestoreInstanceState(state);
    }

    private final String VIEW_SRC_DATA = "Src Data";
    private final String VIEW_BITMAP = "View Path";
    private final String VIEW_SHADOW_EXIST = "DRAW VIEW SHADOW";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putParcelable(VIEW_SRC_DATA, super.onSaveInstanceState());
        bundle.putParcelable(VIEW_BITMAP, mForeImageBitmap);
        bundle.putBoolean(VIEW_SHADOW_EXIST, mShadowLineExist);

        return bundle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            mPath.moveTo(event.getX(), event.getY());
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            mPath.lineTo(event.getX(), event.getY());
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            mPath.lineTo(event.getX(), event.getY());
        }
        invalidate();

        return true;
    }
}
