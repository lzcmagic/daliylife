package com.example.lzc.daliylife.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.fragments.fuli.AndroidFragment;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.normalUtil.DensityUtils;
import com.example.lzc.daliylife.normalUtil.ScreenUtils;

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
        Glide.with(fragment)
                //加载500像素的图片
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.loading)
                .centerCrop()
                .placeholder(R.mipmap.icon_gb_dengdai)
                .error(R.mipmap.fail_load)
                .into(view);
    }
}
