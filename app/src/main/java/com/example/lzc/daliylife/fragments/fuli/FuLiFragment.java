package com.example.lzc.daliylife.fragments.fuli;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.entity.FuLiEntity;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.normalUtil.T;
import com.example.lzc.daliylife.utils.HttpMethods;
import com.example.lzc.daliylife.views.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * Created by lzc on 2017/2/7.
 */

public class FuLiFragment extends Fragment {
    Unbinder unBinder;
    @BindView(R.id.hot_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.spl_refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    private FuLiAdapter mFuLiAdapter;
    private List<FuLiEntity.Result> FuLiLists;
    private int Number = 10;
    //从1开始
    private int Page = 1;
    //Layout布局
    private LinearLayoutManager mLinearManager;
    private int LastVisiblePosition;
    private boolean IsRefreshFinish = false;
    private boolean IsDataRefresh = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hot_news_fragment, null);
        unBinder = ButterKnife.bind(this, rootView);
        FuLiLists = new ArrayList<>();
        initSwipeLayout();
        mLinearManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearManager);
        mFuLiAdapter = new FuLiAdapter();
        recyclerView.setAdapter(mFuLiAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = recyclerView.getVerticalScrollbarPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        mFuLiAdapter.getItemCount() == LastVisiblePosition + 1 &&
                        !IsRefreshFinish) {
                    if (IsDataRefresh)
                        return;
                    //执行刷新
                    Page++;
                    IsDataRefresh = true;
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LastVisiblePosition = mLinearManager.findLastVisibleItemPosition();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * 初始化下拉刷新
     */
    private void initSwipeLayout() {
        mRefreshLayout.setScrollUpChild(recyclerView);
        mRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!IsDataRefresh) {
                    Page = 1;
                    FuLiLists.clear();
                    IsDataRefresh = true;
                    //加载数据
                    loadData();

                }

            }


        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            initFuLiData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        initFuLiData();
    }

    /**
     * 初始化福利图片
     */
    private void initFuLiData() {
        HttpMethods.getInstance(Constants.GANKMAINAPI).getFuLiInfo(new Subscriber<FuLiEntity>() {

            @Override
            public void onCompleted() {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                IsDataRefresh = false;
                if (FuLiLists.size() < Number) {
                    //加载完毕，(正好加载到第十条)
                    IsRefreshFinish = true;
                }
                mFuLiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                T.showShort(getString(R.string.load_data_abnormal));
                IsDataRefresh = false;
                IsRefreshFinish = true;
                mFuLiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNext(FuLiEntity fuLiEntity) {
                ArrayList<FuLiEntity.Result> results = fuLiEntity.getResults();
                if (results != null && results.size() > 0) {
                    for (FuLiEntity.Result result : results) {
                        FuLiLists.add(result);
                    }
                }
            }
        }, "福利", Number, Page);
    }

    private class FuLiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater mInflater;
        private int LOAD_MORE = 1;
        private int LOAD_FINISH = 2;

        public FuLiAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //必须用这种方式，否则会导致宽度测量不一致
            if (viewType == LOAD_FINISH) {
                View view = mInflater.inflate(R.layout.hot_news_adapter, parent, false);
                MyHolder myHolder = new MyHolder(view);
                return myHolder;
            } else if (viewType == LOAD_MORE) {
                View view = mInflater.inflate(R.layout.load_more_layout, parent, false);
                LoadMoreHolder holder = new LoadMoreHolder(view);
                return holder;
            } else {
                View view = mInflater.inflate(R.layout.hot_news_adapter, parent, false);
                MyHolder myHolder = new MyHolder(view);
                return myHolder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyHolder) {
                ((MyHolder) holder).fuliText.setText(FuLiLists.get(position).getDesc());
                Glide.with(FuLiFragment.this)
                        //加载500像素的图片
                        .load(FuLiLists.get(position).getUrl() + "?imageView2/0/w/500")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.loading)
                        .error(R.drawable.ic_menu_gallery)
                        .into(((MyHolder) holder).fuliImage);
            } else if (holder instanceof LoadMoreHolder) {
                if (IsRefreshFinish) {
                    ((LoadMoreHolder) holder).mText.setText(getResources().getString(R.string.load_finish));
                    ((LoadMoreHolder) holder).mProgress.setVisibility(View.GONE);
                } else {
                    ((LoadMoreHolder) holder).mText.setText(getResources().getString(R.string.load_more));
                    ((LoadMoreHolder) holder).mProgress.setVisibility(View.VISIBLE);
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
            return FuLiLists.size() + 1;
        }

//        @Override
//        public void onBindViewHolder(MyHolder holder, int position) {
//
//        }

        class MyHolder extends RecyclerView.ViewHolder {
            //            @BindView(R.id.tv_fuli_desc)
            TextView fuliText;
            //            @BindView(R.id.iv_fuli_image)
            ImageView fuliImage;

            public MyHolder(View itemView) {
                super(itemView);
                fuliText = (TextView) itemView.findViewById(R.id.tv_fuli_desc);
                fuliImage = (ImageView) itemView.findViewById(R.id.iv_fuli_image);
//                ButterKnife.bind(this,itemView);
            }
        }

        class LoadMoreHolder extends RecyclerView.ViewHolder {

            private ProgressBar mProgress;
            private TextView mText;

            public LoadMoreHolder(View itemView) {
                super(itemView);
                mProgress = (ProgressBar) itemView.findViewById(R.id.pb_load);
                mText = (TextView) itemView.findViewById(R.id.tv_load);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();
    }
}
