package com.example.lzc.daliylife.framework;

import android.app.Application;
import android.content.Context;

import com.example.lzc.daliylife.entity.LocationEntity;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by lzc on 2016/12/5.
 */

public class ApplWork extends Application {
    public static Context ApplWorkContext;
    public static LocationEntity CurrentLocation;


    {
        PlatformConfig.setWeixin("","");
        PlatformConfig.setQQZone("1106055064","idKHzPMspLPDt2fK");
        PlatformConfig.setSinaWeibo("3250365559","200f38ce5d9eaf91a19c7a30299cbecd","");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplWorkContext=getApplicationContext();
        //友盟分享
        //TODO:记得关闭
        Config.DEBUG = true;
        UMShareAPI.get(this);
        CrashReport.initCrashReport(this,"fe650da1ba",true);
        //tencent webview
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
    }

    public static Context getApplWorkContext(){
        return ApplWorkContext;
    }
}
