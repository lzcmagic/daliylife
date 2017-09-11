package com.lzc.daliylife.gank;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.BaseAdapter;
import com.lzc.daliylife.adapter.BaseViewHolder;
import com.lzc.daliylife.entity.gankentity.Result;
import com.lzc.daliylife.utils.DateTimeFormat;
import com.lzc.daliylife.utils.GlideUtils;

/**
 * Created by lzc on 2017/9/8.
 *
 */

public class NormalAdapter extends BaseAdapter<Result,NormalHolder> {

    private Context context;
    private int mLastPosition = -1;
    public NormalAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        addAnimation(holder);
    }

    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (holder.getLayoutPosition() > mLastPosition) {
            startAnim(ObjectAnimator.ofFloat(holder.itemView,
                    "translationX", holder.itemView.getRootView().getWidth(), 0));
            mLastPosition = holder.getLayoutPosition();
        }
    }

    private void startAnim(Animator anim) {
        anim.setDuration(500).start();
        anim.setInterpolator(new DecelerateInterpolator());
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

