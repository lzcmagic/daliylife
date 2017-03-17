package com.example.lzc.daliylife.framework;

import android.app.Application;
import android.content.Context;

import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by lzc on 2016/12/5.
 */

public class ApplWork extends Application {
    public static Context ApplWorkContext;
    @Override
    public void onCreate() {
        super.onCreate();
        ApplWorkContext=getApplicationContext();
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
    }

    public void getApplicationLongTime(){
    }

    public static Context getApplWorkContext(){
        return ApplWorkContext;
    }
}
