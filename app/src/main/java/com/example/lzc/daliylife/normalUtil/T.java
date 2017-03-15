package com.example.lzc.daliylife.normalUtil;

import android.widget.Toast;

import com.example.lzc.daliylife.framework.ApplWork;

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
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(int message) {
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
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(int message) {
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

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(int message, int duration) {
        if (isShow)
            toast(message, duration);
    }

    public static void toast(CharSequence content, int time) {
        if (toast == null) {
            toast = Toast.makeText(ApplWork.getApplWorkContext(), content, time);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void toast(int resId, int time) {
        toast(ApplWork.getApplWorkContext().getString(resId), time);
    }
}
