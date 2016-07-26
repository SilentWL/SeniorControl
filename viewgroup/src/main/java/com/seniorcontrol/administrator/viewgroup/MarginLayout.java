package com.seniorcontrol.administrator.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class MarginLayout extends ViewGroup{
    public MarginLayout(Context context) {
        super(context);
    }

    public MarginLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarginLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int twidth = 0;
        int bwidth = 0;
        int lheight = 0;
        int rheight = 0;
        int count = getChildCount();
        MarginLayoutParams params = null;

        for (int i = 0; i < count; i++){
            View childView = getChildAt(i);
            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();
            params = (MarginLayoutParams)childView.getLayoutParams();

            if (i == 0 || i == 1){
                twidth += width + params.leftMargin + params.rightMargin;
            }
            if (i == 2 || i == 3){
                bwidth += width + params.leftMargin + params.rightMargin;
            }
            if (i == 0 || i == 2){
                lheight += height + params.topMargin + params.bottomMargin;
            }
            if (i ==1 || i == 3){
                rheight += height + params.topMargin + params.bottomMargin;
            }
        }

        int width = Math.max(twidth, bwidth);
        int height = Math.max(lheight, rheight);

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth : width, (heightMode == MeasureSpec.EXACTLY)?sizeHeight:height);


    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int width = 0;
        int height = 0;

        MarginLayoutParams params = null;

        for (int i = 0; i < count; i++){
            View view = getChildAt(i);
            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
            params = (MarginLayoutParams) view.getLayoutParams();

            int cl = 0;
            int ct = 0;
            int cr = 0;
            int cb = 0;
            switch (i){
                case 0:
                    cl = params.leftMargin;
                    ct = params.topMargin;
                    break;
                case 1:
                    cl = getWidth() - width - params.leftMargin - params.rightMargin;
                    ct = params.topMargin;
                    break;
                case 2:
                    cl = params.leftMargin;
                    ct = getHeight() - height - params.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - width - params.leftMargin - params.rightMargin;
                    ct = getHeight() - height - params.bottomMargin;
                    break;

            }
            cr = cl + width;
            cb = height + ct;
            view.layout(cl, ct, cr, cb);
        }
    }
}
