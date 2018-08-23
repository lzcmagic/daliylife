package com.lzc.daliylife.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzc.daliylife.R;

/**
 * Created by lzc on 2017/3/15.
 * Glide使用时 ImageViw慎用fitXY的scaleType
 * 用了之后Glide的会缓存所有最大尺寸图片 并不进行压缩加载
 */


public class GlideUtils {


    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadGankRatioImage(Fragment fragment, String url, ImageView view){
        RequestOptions options =new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.icon_gb_dengdai)
                .error(R.mipmap.fail_load);
        Glide.with(fragment)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(view);
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadGankRatioImage(Context context, String url, ImageView view){
        RequestOptions options =new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.icon_gb_dengdai)
                .error(R.mipmap.fail_load);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(view);
    }
}
