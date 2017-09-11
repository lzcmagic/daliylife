package com.lzc.daliylife.utils;

import android.app.Activity;
import android.content.Context;

import com.lzc.daliylife.R;
import com.lzc.daliylife.gank.welfare.WelfareActivity;
import com.tapadoo.alerter.Alerter;

/**
 * Created by lzc on 2017/9/11.
 */

public class AUtil {

    /**
     * 展示提醒框
     * @param context 上下文
     * @param text 文字
     * @param resId 图标id
     */
    public static void showAlert(Activity context,String text,int resId,int bgColor){
        Alerter.create(context)
                .setTitle("提示")
                .setText(text)
                .setBackgroundColor(bgColor)
                .setIcon(resId)
                .setDuration(500)
                .show();
    }

}
