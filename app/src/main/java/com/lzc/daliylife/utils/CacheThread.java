package com.lzc.daliylife.utils;

import android.text.TextUtils;

import com.lzc.daliylife.framework.MyApp;

import java.io.Serializable;

/**
 * Created by lzc on 2017/3/21.
 */

public class CacheThread extends Thread {
    ACache mACache;
    String key;
    Serializable object;

    public CacheThread() {
        mACache = ACache.get(MyApp.ApplWorkContext);
    }

    public void setCacheContent(String key, Serializable value) {
        this.object = value;
        this.key = key;

    }

    @Override
    public void run() {
        super.run();
        if (!TextUtils.isEmpty(key) && object != null)
            mACache.put(key, object, ACache.TIME_DAY);
    }

}
