package com.example.lzc.daliylife.framework;

import android.app.Application;
import android.content.Context;

/**
 * Created by lzc on 2016/12/5.
 */

public class ApplWork extends Application {
    public static Context ApplWorkContext;
    @Override
    public void onCreate() {
        super.onCreate();
        ApplWorkContext=getApplicationContext();
    }

    public static Context getApplWorkContext(){
        return ApplWorkContext;
    }
}
