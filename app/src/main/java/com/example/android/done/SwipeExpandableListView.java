package com.example.android.done;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

/**
 * Created by peng on 2018/4/18.
 */

public class SwipeExpandableListView extends ExpandableListView {
    private int mScreenWidth;   //
    private int mDownX;         //
    private int mDownY;         //
    private int mDeleteBtnWidth;//

    private boolean isDeleteShown;  //

    private ViewGroup mPointChild;  //
    private LinearLayout.LayoutParams mLayoutParams;    //

    public SwipeExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE://
                super.onTouchEvent(ev);//
                return performActionMove(ev);
            case MotionEvent.ACTION_UP://
                performActionUp();
                break;
        }
        return super.onTouchEvent(ev);
    }

    //
    private void performActionDown(MotionEvent ev) {
        Log.v("performActionDown", "Hello!");
        if(isDeleteShown) {
            turnToNormal();
        }

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        Log.v("x", String.valueOf(mDownX));
        Log.v("y", String.valueOf(mDownY));
        //
        mPointChild = (ViewGroup) this.getChildAt(pointToPosition(mDownX, mDownY)
                - getFirstVisiblePosition());
        //
        mDeleteBtnWidth = mPointChild.getChildAt(1).getLayoutParams().width;
        mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                .getLayoutParams();
        mLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    //
    private boolean performActionMove(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();
        if(Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
            //
            if(nowX < mDownX) {
                //
                int scroll = (nowX - mDownX) / 2;
                //
                if(-scroll >= mDeleteBtnWidth) {
                    scroll = -mDeleteBtnWidth;
                }
                //
                mLayoutParams.leftMargin = scroll;
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    //
    private void performActionUp() {
        Log.v("atIndex", String.valueOf(pointToPosition(mDownX, mDownY)
                - getFirstVisiblePosition()));
        Log.v("mPointChild", String.valueOf(mPointChild));
        Log.v("mLayoutParams", String.valueOf(mLayoutParams));
        if(-mLayoutParams.leftMargin >= mDeleteBtnWidth / 2) {
            mLayoutParams.leftMargin = -mDeleteBtnWidth;
            isDeleteShown = true;
        }else {
            turnToNormal();
        }

        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    public void turnToNormal() {
        mLayoutParams.leftMargin = 0;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        isDeleteShown = false;
    }
    public boolean canClick() {
        return !isDeleteShown;
    }
}
