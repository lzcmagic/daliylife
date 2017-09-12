package com.lzc.daliylife.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.normalUtil.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lzc on 2016/12/7.
 */

public abstract class BaseAdapter<V, T extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    private List<V> mDatas;
    private LayoutInflater mInflater;
    private int LOAD_MORE = 1;
    private int LOAD_FINISH = 2;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private int LastVisiblePosition = 0;
    private Context mContext;
    private boolean IsLoadFinish;//数据是否全部加载完毕

    public BaseAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    /**
     * 设置下拉刷新
     *
     * @param listener     刷新回调
     * @param recyclerView recyclerView
     */
    public void setLoadMoreRecyclerView(final OnRecyclerScrolledListener listener,
                                        RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                L.d("newState:"+newState+"   getItemCount(): "+ getItemCount());
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        getItemCount() == LastVisiblePosition + 1 &&
                        !IsLoadFinish) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onLoadMoreRequested();
                            }
                        }
                    }, 1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager instanceof LinearLayoutManager) {
                    LastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    L.d("LastVisiblePosition: "+LastVisiblePosition);
                }
                if (layoutManager instanceof StaggeredGridLayoutManager){
                    int[] into=new int[((StaggeredGridLayoutManager)layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager)layoutManager).findLastVisibleItemPositions(into);
                    LastVisiblePosition =findMax(into);
                    L.d("LastVisiblePosition: "+LastVisiblePosition);
                }
            }
        });
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    /**
     * 停止加载数据
     */
    public void stopLoadData() {
        IsLoadFinish = true;
    }

    /**
     * 填充数据
     *
     * @param datas 数据
     */
    public void setData(List<V> datas) {
        if (datas != null) {
            this.mDatas = datas;
        } else {
            this.mDatas = new ArrayList<>();
        }
    }


    /**
     * 获得需要展示的viewHolder
     *
     * @return viewHolder
     */
    public abstract T getViewHolder(LayoutInflater mInflater, ViewGroup parent);

    public abstract void initNormalHolder(V value,T holder);

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE) {
            View view = mInflater.inflate(R.layout.load_more_layout, parent, false);
            return  new FooterHolder(view);

        } else if (viewType == LOAD_FINISH) {
            return getViewHolder(mInflater, parent);
        } else {
            View view = mInflater.inflate(R.layout.load_more_layout, parent, false);
            return new FooterHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {

        int viewType = holder.getItemViewType();
        if (viewType == LOAD_FINISH) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder, holder.getAdapterPosition());
                }
            });
            initNormalHolder(mDatas.get(position), (T) holder);
        } else {
            if (IsLoadFinish) {
                ((FooterHolder) holder).mProgress.setVisibility(View.GONE);
                ((FooterHolder) holder).mText.setText(mContext.getResources().getString(R.string.load_finish));
            } else {
                ((FooterHolder) holder).mProgress.setVisibility(View.VISIBLE);
                ((FooterHolder) holder).mText.setText(mContext.getResources().getString(R.string.load_more));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == this.getItemCount()) {
            return LOAD_MORE;
        } else {
            return LOAD_FINISH;
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size() + 1;
        }
        return 0;
    }


    class FooterHolder extends BaseViewHolder {
        @BindView(R.id.pb_load)
        ProgressBar mProgress;
        @BindView(R.id.tv_load)
        TextView mText;

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

}
