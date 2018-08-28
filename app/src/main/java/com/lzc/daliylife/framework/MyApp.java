package com.lzc.daliylife.framework;

import android.app.Application;
import android.content.Context;

import com.lzc.daliylife.di.base.AppComponent;
import com.lzc.daliylife.di.base.DaggerAppComponent;
import com.tencent.bugly.crashreport.CrashReport;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by lzc on 2016/12/5.
 *
 */

public class MyApp extends DaggerApplication {
    public static Context ApplWorkContext;



    @Override
    public void onCreate() {
        super.onCreate();
        ApplWorkContext=getApplicationContext();
        CrashReport.initCrashReport(this,"fe650da1ba",false);

    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent build = DaggerAppComponent.builder().application(this).build();
        build.inject(this);
        return build;
    }

    public static Context getApplWorkContext(){
        return ApplWorkContext;
    }
}
