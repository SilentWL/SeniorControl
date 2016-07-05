package com.seniorcontrol.administrator.gobang;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GobangView extends View implements View.OnTouchListener{
    private final int CHESS_PIECES_WIDTH = 20; //DP
    private final int CHESS_PIECES_SPACING = 30; //DP
    private final int VERTICAL_GRID_COUNT = 13;
    private final int HORIZONTAL_GRID_COUNT = 13;
    private final int GRID_WIDTH = 5; //DP
    private final int GRID_COLOR = 0Xff000000;

    private final int CHESS_PIECES_WIN_COUNT = 5;
    private final int WIN_MSG_ID = 10;

    private int mChessPiecesWidth = dp2Px(CHESS_PIECES_WIDTH);
    private int mChessPiecesSpacing = dp2Px(CHESS_PIECES_SPACING);
    private int mVerticalGridCount = VERTICAL_GRID_COUNT;
    private int mHorizontalGridCount = HORIZONTAL_GRID_COUNT;
    private int mGridWidth = dp2Px(GRID_WIDTH);
    private int mGridColor = GRID_COLOR;
    private int mChessBoardWidth = 0;
    private int mChessBoardHeight = 0;

    private int mDrawVerticalGridCount = 0;
    private int mDrawHorizontalGridCount = 0;
    private boolean mIsWhiteChessPiece = true;
    private Paint mPaint = new Paint();
    private Bitmap mWhitePiece = null;
    private Bitmap mBlackPiece = null;
    private boolean mGameOver = false;
    private boolean mWhiteWinner = true;

    private boolean mIsCheckWinner = false;
    private Thread mThread = null;
    private Handler mHandler = null;

    private List<ChessPiecesType> mChessPiecesTypes = new ArrayList<ChessPiecesType>();

    private class ChessPiecesType{
        public Point chessPiecesXY;
        public boolean whiteChessPiece;

        public ChessPiecesType(){
        }
        public ChessPiecesType(ChessPiecesType type){
            this.chessPiecesXY = new Point(type.chessPiecesXY);
            this.whiteChessPiece = type.whiteChessPiece;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ChessPiecesType chessPiecesType = (ChessPiecesType) o;

            if (!chessPiecesXY.equals(chessPiecesType.chessPiecesXY)) return false;
            if (whiteChessPiece != chessPiecesType.whiteChessPiece) return false;

            return true;
        }
    }

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

        this.setOnTouchListener(this);
        //initGridLinePaint();
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_white);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_black);

        mThread = new Thread(new CheckWinnerRunnable());
        mThread.start();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == WIN_MSG_ID) {
                    if (mGameOver) {
                        String mWinString;
                        mWinString = (mWhiteWinner) ? new String("White Piece Win") : new String("Black Piece Win");
                        Toast.makeText(getContext(), mWinString, Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

    }

    private void initGridLinePaint() {
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
                    mChessPiecesSpacing = typedArray.getDimensionPixelSize(typedIndex, mChessPiecesSpacing);
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

        ViewGroup parentView = ((ViewGroup)getParent());
        int parentHeight = parentView.getHeight();
        int parentWidth = parentView.getWidth();
        int wrapContentWidth = (mVerticalGridCount - 1) * mChessPiecesSpacing + mGridWidth + getPaddingLeft() + getPaddingRight();
        int wrapContentHeight = (mHorizontalGridCount - 1) * mChessPiecesSpacing + mGridWidth + getPaddingTop() + getPaddingBottom();
        int width = getUserSpecWidth(widthSize, widthMode, parentWidth, wrapContentWidth);
        int height = getUserSpecHeight(heightSize, heightMode, parentHeight, wrapContentHeight);

        mChessBoardWidth = getApplicableWidth(parentWidth, width, wrapContentWidth);
        mChessBoardHeight = getApplicableHeight(parentHeight, height, wrapContentHeight);

        setMeasuredDimension(mChessBoardWidth, mChessBoardHeight);
    }

    private int getApplicableHeight(int parentHeight, int height, int wrapContentHeight) {
        if (height <= parentHeight){
            if (wrapContentHeight <= height) {
                mDrawHorizontalGridCount = (wrapContentHeight - getPaddingTop() - getPaddingBottom() - mGridWidth) / mChessPiecesSpacing + 1;
            }else{
                mDrawHorizontalGridCount = (height - getPaddingTop() - getPaddingBottom() - mGridWidth) / mChessPiecesSpacing + 1;
            }
        }else{
            mDrawHorizontalGridCount = (parentHeight - getPaddingTop() - getPaddingBottom() - mGridWidth) / mChessPiecesSpacing + 1;
        }
        height = (mDrawHorizontalGridCount - 1) * mChessPiecesSpacing + getPaddingTop() + getPaddingBottom() + mGridWidth;
        return height;
    }

    private int getApplicableWidth(int parentWidth, int width, int wrapContentWidth) {
        if (width <= parentWidth){
            if (wrapContentWidth <= width){
                mDrawVerticalGridCount = (wrapContentWidth - getPaddingLeft() - getPaddingRight() - mGridWidth) / mChessPiecesSpacing + 1;
            }else {
                mDrawVerticalGridCount = (width - getPaddingLeft() - getPaddingRight() - mGridWidth) / mChessPiecesSpacing + 1;
            }
        }
        else{
            mDrawVerticalGridCount = (parentWidth - getPaddingLeft() - getPaddingRight() - mGridWidth) / mChessPiecesSpacing + 1;
        }
        width = (mDrawVerticalGridCount - 1) * mChessPiecesSpacing + getPaddingLeft() + getPaddingRight() + mGridWidth;
        return width;
    }

    private int getUserSpecHeight(int heightSize, int heightMode, int parentHeight, int wrapContentHeight) {
        int height;
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            if (heightMode == MeasureSpec.AT_MOST){
                height = wrapContentHeight;
            }else{
                height = parentHeight;
            }
            height = Math.max(height, heightSize);
        }
        return height;
    }

    private int getUserSpecWidth(int widthSize, int widthMode, int parentWidth, int wrapContentWidth) {
        int width;
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            if (widthMode == MeasureSpec.AT_MOST){
                width = wrapContentWidth;
            }
            else {

                width = parentWidth;
            }
            width = Math.max(width, widthSize);
        }
        return width;
    }
    private class CheckWinnerRunnable implements Runnable{
        @Override
        public void run() {
            while (true) {
                if (mIsCheckWinner){
                    judgeWinner();
                    mIsCheckWinner = false;
                }

                if (mGameOver) {
                    mHandler.sendEmptyMessage(WIN_MSG_ID);
                    return;
                }
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft() + mGridWidth / 2, getPaddingTop() + mGridWidth / 2);
        drawGridLine(canvas);
        drawCheces(canvas);
        canvas.restore();
    }

    private synchronized void judgeWinner() {
        int chessPiecesCount = mChessPiecesTypes.size();
        for(int i = 0; i < chessPiecesCount; i++){

            if (checkVerticalPieces(i) >= CHESS_PIECES_WIN_COUNT - 1 || checkHorizontalPieces(i) >= CHESS_PIECES_WIN_COUNT - 1 || checkLeftPosteriorPieces(i) >= CHESS_PIECES_WIN_COUNT - 1 || checkRightPosteriorPieces(i) >= CHESS_PIECES_WIN_COUNT - 1){
                mGameOver = true;
                mWhiteWinner = mChessPiecesTypes.get(i).whiteChessPiece;
                return;
            }

        }
    }

    private int checkVerticalPieces(int i){
        int totalCount = 0;

        ChessPiecesType chessPiecesType = new ChessPiecesType(mChessPiecesTypes.get(i));
        for (int j = 0; j < CHESS_PIECES_WIN_COUNT; j++) {
            chessPiecesType.chessPiecesXY.y -= mChessPiecesSpacing;
            if (mChessPiecesTypes.contains(chessPiecesType)){
                totalCount++;
            }
        }
        chessPiecesType.chessPiecesXY.y = mChessPiecesTypes.get(i).chessPiecesXY.y;
        for (int k = 0; k < CHESS_PIECES_WIN_COUNT; k++){
            chessPiecesType.chessPiecesXY.y += mChessPiecesSpacing;
            if (mChessPiecesTypes.contains(chessPiecesType)){
                totalCount++;
            }
        }

        return totalCount;
    }
    private int checkRightPosteriorPieces(int i) {
        int totalCount = 0;

        ChessPiecesType chessPiecesType = new ChessPiecesType(mChessPiecesTypes.get(i));
        for (int j = 0; j < CHESS_PIECES_WIN_COUNT; j++) {
            chessPiecesType.chessPiecesXY.x -= mChessPiecesSpacing;
            chessPiecesType.chessPiecesXY.y += mChessPiecesSpacing;
            if (mChessPiecesTypes.contains(chessPiecesType)){
                totalCount++;
            }
        }
        chessPiecesType.chessPiecesXY.x = mChessPiecesTypes.get(i).chessPiecesXY.x;
        chessPiecesType.chessPiecesXY.y = mChessPiecesTypes.get(i).chessPiecesXY.y;
        for (int k = 0; k < CHESS_PIECES_WIN_COUNT; k++){
            chessPiecesType.chessPiecesXY.x += mChessPiecesSpacing;
            chessPiecesType.chessPiecesXY.y -= mChessPiecesSpacing;
            if (mChessPiecesTypes.contains(chessPiecesType)){
                totalCount++;
            }
        }

        return totalCount;
    }

    private int checkLeftPosteriorPieces(int i) {
        int totalCount = 0;

        ChessPiecesType chessPiecesType = new ChessPiecesType(mChessPiecesTypes.get(i));
        for (int j = 0; j < CHESS_PIECES_WIN_COUNT; j++) {
            chessPiecesType.chessPiecesXY.x -= mChessPiecesSpacing;
            chessPiecesType.chessPiecesXY.y -= mChessPiecesSpacing;
            if (mChessPiecesTypes.contains(chessPiecesType)){
                totalCount++;
            }
        }
        chessPiecesType.chessPiecesXY.x = mChessPiecesTypes.get(i).chessPiecesXY.x;
        chessPiecesType.chessPiecesXY.y = mChessPiecesTypes.get(i).chessPiecesXY.y;
        for (int k = 0; k < CHESS_PIECES_WIN_COUNT; k++){
            chessPiecesType.chessPiecesXY.x += mChessPiecesSpacing;
            chessPiecesType.chessPiecesXY.y += mChessPiecesSpacing;
            if (mChessPiecesTypes.contains(chessPiecesType)){
                totalCount++;
            }
        }

        return totalCount;
    }

    private int  checkHorizontalPieces(int i) {
        int totalCount = 0;
        ChessPiecesType chessPiecesType = new ChessPiecesType(mChessPiecesTypes.get(i));
        for (int j = 0; j < CHESS_PIECES_WIN_COUNT; j++) {
            chessPiecesType.chessPiecesXY.x -= mChessPiecesSpacing;
            if (mChessPiecesTypes.contains(chessPiecesType)){
                totalCount++;
            }
        }
        chessPiecesType.chessPiecesXY.x = mChessPiecesTypes.get(i).chessPiecesXY.x;
        for (int k = 0; k < CHESS_PIECES_WIN_COUNT; k++){
            chessPiecesType.chessPiecesXY.x += mChessPiecesSpacing;
            if (mChessPiecesTypes.contains(chessPiecesType)){
                totalCount++;
            }
        }
        return totalCount;
    }

    private void drawCheces(Canvas canvas) {
        Rect rect = new Rect();
        mPaint.reset();
        for(ChessPiecesType chessPiecesType:mChessPiecesTypes){
            rect.left = chessPiecesType.chessPiecesXY.x;
            rect.top = chessPiecesType.chessPiecesXY.y;
            rect.right = chessPiecesType.chessPiecesXY.x + mChessPiecesWidth;
            rect.bottom = chessPiecesType.chessPiecesXY.y + mChessPiecesWidth;

            if (chessPiecesType.whiteChessPiece){
                canvas.drawBitmap(mWhitePiece, null, rect, mPaint);
            }else {
                canvas.drawBitmap(mBlackPiece, null, rect, mPaint);
            }

        }
    }

    private void drawGridLine(Canvas canvas) {
        initGridLinePaint();
        for (int i = 0; i < mHorizontalGridCount; i++){
            canvas.drawLine(0, i * mChessPiecesSpacing, getWidth() - getPaddingLeft() - getPaddingRight() - mGridWidth, i * mChessPiecesSpacing, mPaint);
        }
        for (int j = 0; j < mDrawVerticalGridCount; j++){
            canvas.drawLine(j * mChessPiecesSpacing, 0, j * mChessPiecesSpacing, getHeight() - getPaddingTop() - getPaddingBottom() - mGridWidth, mPaint);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Point point = new Point();
        ChessPiecesType chessPiecesType = new ChessPiecesType();

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (mGameOver){
                return false;
            }
            point.x = ((int)(((event.getX() - getPaddingLeft() - mGridWidth / 2) / mChessPiecesSpacing) + 0.5)) * mChessPiecesSpacing  - mChessPiecesWidth / 2;
            point.y = ((int)(((event.getY() - getPaddingTop() - mGridWidth / 2) / mChessPiecesSpacing) + 0.5)) * mChessPiecesSpacing   - mChessPiecesWidth / 2;

            for(ChessPiecesType tempChessPiecesType : mChessPiecesTypes){
                if (tempChessPiecesType.chessPiecesXY.equals(point)){
                    return false;
                }
            }
            chessPiecesType.chessPiecesXY = point;
            chessPiecesType.whiteChessPiece = mIsWhiteChessPiece;
            mChessPiecesTypes.add(chessPiecesType);
            mIsWhiteChessPiece = !mIsWhiteChessPiece;
            mIsCheckWinner = true;
            invalidate();
        }
        return true;
    }
    public synchronized void restartGame(){
        mChessPiecesTypes.clear();
        mGameOver = false;
        mIsWhiteChessPiece = true;
        if (!mThread.isAlive()) {
            mThread = new Thread(new CheckWinnerRunnable());
            mThread.start();
        }
        invalidate();
    }
    public synchronized void backPieces(){
        if (!mGameOver && mChessPiecesTypes.size() > 0) {
            mChessPiecesTypes.remove(mChessPiecesTypes.size() - 1);
            mIsWhiteChessPiece = !mIsWhiteChessPiece;
            invalidate();
        }
    }

}