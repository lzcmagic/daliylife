package com.example.lzc.retrofitandrxjavatest.framework;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by lzc on 2016/12/5.
 */

public class ApplWork extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
