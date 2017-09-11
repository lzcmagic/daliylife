package com.lzc.daliylife.framework;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by lzc on 2016/12/5.
 *
 */

public class ApplWork extends Application {
    public static Context ApplWorkContext;



    @Override
    public void onCreate() {
        super.onCreate();
        ApplWorkContext=getApplicationContext();
        CrashReport.initCrashReport(this,"fe650da1ba",false);

    }

    public static Context getApplWorkContext(){
        return ApplWorkContext;
    }
}
