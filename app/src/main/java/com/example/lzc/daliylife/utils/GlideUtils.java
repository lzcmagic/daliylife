package com.example.lzc.daliylife.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lzc.daliylife.R;
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
    public static void loadIntoUseFitWidth(final Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getWidth();
                        int vh = Math.round(resource.getHeight() * scale);
                        if (vw < DensityUtils.dp2px(context, 200f)) {
                            params.width = ScreenUtils.getScreenWidth(context)-DensityUtils.dp2px(context,20f);
                        } else {
                            params.width = vw;
                        }
                        if (vh > 120) {
                            params.height = DensityUtils.dp2px(context, 120f);
                        } else if (vh < 60) {
                            params.height = DensityUtils.dp2px(context, 120f);
                        } else {
                            params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        }
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(final Fragment context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getWidth();
                        int vh = Math.round(resource.getHeight() * scale);
                        if (vw < DensityUtils.dp2px(context.getContext(), 100f)) {
                            params.width = ScreenUtils.getScreenWidth(context.getContext())-DensityUtils.dp2px(context.getContext(),20f);
                        } else {
                            params.width = vw;
                        }
                        if (vh > 120) {
                            params.height = DensityUtils.dp2px(context.getContext(), 120f);
                        } else if (vh < 60) {
                            params.height = DensityUtils.dp2px(context.getContext(), 120f);
                        } else {
                            params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        }
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(R.mipmap.loading)
                .error(errorImageId)
                .into(imageView);
    }
}
