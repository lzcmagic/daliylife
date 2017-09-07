package com.lzc.daliylife.base;

import android.support.v4.app.FragmentActivity;

/**
 * Created by lzc on 2017/9/7.
 */

public interface BasePresenter {
    void attachView(FragmentActivity fmActivity);
    void detachView();
}
