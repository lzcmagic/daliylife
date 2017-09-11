package com.lzc.daliylife.gank;

import android.view.View;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.BaseViewHolder;
import com.lzc.daliylife.views.RatioImageView;

import butterknife.BindView;

/**
 * Created by lzc on 2017/9/8.
 */

public class NormalHolder extends BaseViewHolder {

    @BindView(R.id.tv_android_title)
    public TextView mTitle;
    @BindView(R.id.tv_android_user)
    public TextView mUser;
    @BindView(R.id.tv_android_date)
    public TextView mDate;
    @BindView(R.id.iv_android_image)
    public RatioImageView mImage;

    public NormalHolder(View itemView) {
        super(itemView);
        mImage.setOriginalSize(70,40);
    }
}