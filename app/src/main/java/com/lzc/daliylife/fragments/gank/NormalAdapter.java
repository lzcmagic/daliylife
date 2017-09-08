package com.lzc.daliylife.fragments.gank;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.BaseAdapter;
import com.lzc.daliylife.adapter.BaseViewHolder;
import com.lzc.daliylife.entity.gankentity.Result;
import com.lzc.daliylife.fragments.gank.NormalHolder;
import com.lzc.daliylife.utils.DateTimeFormat;
import com.lzc.daliylife.utils.GlideUtils;

/**
 * Created by lzc on 2017/9/8.
 *
 */

public class NormalAdapter extends BaseAdapter<Result,NormalHolder> {

    private Context context;
    public NormalAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public NormalHolder getViewHolder(LayoutInflater mInflater, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.android_fragment, parent, false);
        return new NormalHolder(view);
    }

    @Override
    public void initNormalHolder(Result result,BaseViewHolder holder) {
        ((NormalHolder) holder).mTitle.setText(result.getDesc());
        ((NormalHolder) holder).mUser.setText("作者: " + result.getWho());
        ((NormalHolder) holder).mDate.setText(DateTimeFormat
                .formatDateTime(result.getPublishedAt()));
        if (result.getImages() != null && result.getImages().size() > 0) {
            String images = result.getImages().get(0);
            if (!TextUtils.isEmpty(images)) {
                ((NormalHolder) holder).mImage.setVisibility(View.VISIBLE);
                GlideUtils.loadGankRatioImage(context,
                        images + "?imageView2/0/w/500",
                        ((NormalHolder) holder).mImage);
            } else {
                ((NormalHolder) holder).mImage.setVisibility(View.GONE);
            }
        } else {
            ((NormalHolder) holder).mImage.setVisibility(View.GONE);
        }
    }

}

