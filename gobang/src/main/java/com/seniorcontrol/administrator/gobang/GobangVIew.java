package com.seniorcontrol.administrator.gobang;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


public class GobangView extends View{
    private final int CHESS_PIECES_WIDTH = 20; //DP
    private final int CHESS_PIECES_SPACING = 30; //DP
    private final int VERTICAL_GRID_COUNT = 13;
    private final int HORIZONTAL_GRID_COUNT = 13;
    private final int GRID_WIDTH = 5; //DP
    private final int GRID_COLOR = 0Xff000000;

    private int mChessPiecesWidth = dp2Px(CHESS_PIECES_WIDTH);
    private int mChessPiecesSpaceing = dp2Px(CHESS_PIECES_SPACING);
    private int mVerticalGridCount = VERTICAL_GRID_COUNT;
    private int mHorizontalGridCount = HORIZONTAL_GRID_COUNT;
    private int mGridWidth = dp2Px(GRID_WIDTH);
    private int mGridColor = GRID_COLOR;
    private int mChessBoardWidth = 0;
    private int mChessBoardHeight = 0;

    private Paint mPaint = new Paint();

    private int dp2Px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public GobangView(Context context) {
        this(context, null);
    }
    public GobangView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GobangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs, defStyleAttr);

        initPaint();

    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mGridWidth);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mGridColor);
        mPaint.setDither(true);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GobangView, defStyleAttr, 0);

        int typedCount = typedArray.getIndexCount();

        for(int i = 0; i < typedCount; i++){
            int typedIndex = typedArray.getIndex(i);

            switch (typedIndex){
                case R.styleable.GobangView_chessPiecesWidth:
                    mChessPiecesWidth = typedArray.getDimensionPixelSize(typedIndex, mChessPiecesWidth);
                    break;
                case R.styleable.GobangView_chessPiecesSpacing:
                    mChessPiecesSpaceing = typedArray.getDimensionPixelSize(typedIndex, mChessPiecesSpaceing);
                    break;
                case R.styleable.GobangView_verticalGridCount:
                    mVerticalGridCount = typedArray.getInteger(typedIndex, mVerticalGridCount);
                    break;
                case R.styleable.GobangView_horizontalGridCount:
                    mHorizontalGridCount = typedArray.getInteger(typedIndex, mHorizontalGridCount);
                    break;
                case R.styleable.GobangView_gridWidth:
                    mGridWidth = typedArray.getDimensionPixelSize(typedIndex, mGridWidth);
                    break;
                case R.styleable.GobangView_gridColor:
                    mGridColor = typedArray.getColor(typedIndex, mGridColor);
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            if (width == MeasureSpec.AT_MOST){
                width = (mHorizontalGridCount - 1) * mChessPiecesSpaceing + mChessPiecesWidth;
            }
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
    }
}