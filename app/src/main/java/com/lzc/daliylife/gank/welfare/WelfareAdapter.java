package com.lzc.daliylife.gank.welfare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.BaseAdapter;
import com.lzc.daliylife.adapter.BaseViewHolder;
import com.lzc.daliylife.entity.gankentity.FuLiEntity;
import com.lzc.daliylife.views.RatioImageView;

import butterknife.BindView;

/**
 * Created by lzc on 2017/9/8.
 *
 */

public class WelfareAdapter extends BaseAdapter<FuLiEntity.Result,WelfareAdapter.FuliHolder> {


    private Context mContext;
    public WelfareAdapter(Context context) {
        super(context);
        this.mContext=context;
    }

    @Override
    public FuliHolder getViewHolder(LayoutInflater mInflater, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.gank_welfare_adapter, parent, false);
        return new FuliHolder(view);
    }

    @Override
    public void initNormalHolder(FuLiEntity.Result value, BaseViewHolder holder) {

        ((FuliHolder) holder).fuliText.setText(value.getDesc());
        Glide.with(mContext)
                //加载500像素的图片
                .load(value.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.fail_load)
                .into(((FuliHolder) holder).fuliImage);
    }

    class FuliHolder extends BaseViewHolder {
        @BindView(R.id.tv_fuli_desc)
        TextView fuliText;
        @BindView(R.id.iv_fuli_image)
        RatioImageView fuliImage;

        public FuliHolder(View itemView) {
            super(itemView);
            //4:5
            fuliImage.setOriginalSize(40, 50);
        }
    }
}
