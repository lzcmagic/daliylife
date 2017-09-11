package com.lzc.daliylife.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by lzc on 2017/4/6.
 *
 */

public class LotteryLayout extends RelativeLayout {
    public LotteryLayout(Context context) {
        super(context);
    }

    public LotteryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
