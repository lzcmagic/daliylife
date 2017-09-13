package com.lzc.daliylife.normalUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.TypedValue;

/**
 * Created by lzc on 2017/3/13.
 */

public class DensityUtils {
    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }


    /**
     * 按照一定的宽高比例裁剪图片
     *
     * @param bitmap 原始图片
     * @param ratio  height/width
     * @return btiamp
     */
    public static Bitmap ImageCrop(Bitmap bitmap, float ratio, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        nh = (int) (w * ratio);
        nw = w;
        if (nh > h) {
            nh = h;
            nw = (int) (h / ratio);
        }
        if (w > h) {
            retX = (w - nw) / 2;
            retY = 0;
        } else {
            retX = 0;
            retY = (h - nh) / 2;
        }

//        Matrix matrix = new Matrix();
//        float wratio = (width+100) / (float) nw;
//        float hratio = height / (float) nh;
//        matrix.setScale(wratio, hratio);
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);
        if (!bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bmp;
    }
}
