package com.lzc.daliylife.normalUtil;

import android.os.Looper;
import android.widget.Toast;

import com.lzc.daliylife.framework.MyApp;

/**
 * Created by lzc on 2017/3/13.
 */

public class T {
    private static Toast toast;
    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (isShow)
            toast(message, Toast.LENGTH_SHORT);
    }


    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (isShow)
            toast(message, Toast.LENGTH_LONG);
    }


    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        if (isShow)
            toast(message, duration);
    }


    public static void toast(CharSequence content){
        toast(content,Toast.LENGTH_SHORT);
    }

    private static void toast(CharSequence content, int time) {
        if (Thread.currentThread()!= Looper.getMainLooper().getThread()){
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(MyApp.getApplWorkContext(), content, time);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void toast(int resId) {
        toast(MyApp.getApplWorkContext().getString(resId), Toast.LENGTH_SHORT);
    }
}
