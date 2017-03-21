package com.example.lzc.daliylife.fragments.fuli;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.activity.GankDetailInfo;
import com.example.lzc.daliylife.entity.gankentity.AndroidEntity;
import com.example.lzc.daliylife.entity.gankentity.Result;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.normalUtil.T;
import com.example.lzc.daliylife.utillistener.OnRecyclerViewItemClickListener;
import com.example.lzc.daliylife.utils.DateTimeFormat;
import com.example.lzc.daliylife.utils.GlideUtils;
import com.example.lzc.daliylife.utils.HttpMethods;
import com.example.lzc.daliylife.views.RatioImageView;
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

public class AndroidFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.spl_refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_android)
    RecyclerView mRecyclerView;
    private List<Result> AndroidEntitys;
    private boolean IsRefreshFinish;
    private boolean IsDataRefresh;
    private int Number = 10;
    private int Page = 1;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int LastVisiblePosition;
    private boolean IsFirstLoad = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidEntitys = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.android_info_fragment, null);
        mUnbinder = ButterKnife.bind(this, rootView);
        mAdapter = new MyAdapter();
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        mAdapter.getItemCount() == LastVisiblePosition + 1 &&
                        !IsRefreshFinish) {
                    if (IsDataRefresh)
                        return;
                    Page++;
                    IsDataRefresh = true;
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                        }
                    }, 500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        mAdapter.setOnitemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                Result result = AndroidEntitys.get(position);
                GankDetailInfo.actionIntentStart(getActivity(),
                        result.getDesc(),
                        result.getPublishedAt(),
                        result.getWho(),
                        result.getUrl());
            }
        });
        initRefreshLauyout();
        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    /**
     * 初始化下拉刷新控件
     */
    private void initRefreshLauyout() {
        mRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_blue_dark));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (IsDataRefresh)
                    return;
                //初始化
                AndroidEntitys.clear();
                Page = 1;
                IsDataRefresh = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载数据
                        initData();
                    }
                },1000);

            }
        });
    }

    public void ScrollToTop(){
        mRecyclerView.scrollToPosition(0);
    }

    /**
     * 加载更多数据
     */
    private void loadData() {
        initData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && IsFirstLoad) {
            initData();
            IsFirstLoad = false;
        }

    }

    /**
     * 初始化数据
     */
    private void initData() {
        HttpMethods.getInstance(Constants.GANKMAINAPI).getAndroidInfo(new Subscriber<AndroidEntity>() {
            @Override
            public void onCompleted() {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                IsDataRefresh = false;
                if (AndroidEntitys.size() < Number) {
                    //加载完毕，(正好加载到第十条)
                    IsRefreshFinish = true;
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                T.showShort(getString(R.string.load_data_abnormal));
                IsDataRefresh = false;
                IsRefreshFinish = true;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNext(AndroidEntity androidEntity) {
                ArrayList<Result> results = androidEntity.getResults();
                if (results != null && results.size() > 0) {
                    for (Result result : results) {
                        AndroidEntitys.add(result);
                    }
                }
            }
        }, "Android", Number, Page);
    }

     class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater mInflater;
        private int LOAD_MORE = 1;
        private int LOAD_FINISH = 2;
        private OnRecyclerViewItemClickListener mOnitemClickListener;

        public MyAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /**
         * 初始化接口
         *
         * @param mOnitemClickListener
         */
        public void setOnitemClickListener(OnRecyclerViewItemClickListener mOnitemClickListener) {
            MyAdapter.this.mOnitemClickListener = mOnitemClickListener;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == LOAD_MORE) {
                View view = mInflater.inflate(R.layout.load_more_layout, parent, false);
                FooterHolder holder = new FooterHolder(view);
                return holder;

            } else if (viewType == LOAD_FINISH) {
                View view = mInflater.inflate(R.layout.android_fragment, parent, false);
                NormalHolder holder = new NormalHolder(view);
                return holder;
            } else {
                View view = mInflater.inflate(R.layout.android_fragment, parent, false);
                NormalHolder holder = new NormalHolder(view);
                return holder;
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
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof NormalHolder) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnitemClickListener.onItemClick(holder, position);
                    }
                });
                Result result = AndroidEntitys.get(position);
                ((NormalHolder) holder).mTitle.setText(result.getDesc());
                ((NormalHolder) holder).mUser.setText("作者: " + result.getWho());
                ((NormalHolder) holder).mDate.setText(DateTimeFormat
                        .formatDateTime(result.getPublishedAt()));
                if (result.getImages() != null && result.getImages().size() > 0) {
                    String images = result.getImages().get(0);
                    if (!TextUtils.isEmpty(images)) {
                        ((NormalHolder) holder).mImage.setVisibility(View.VISIBLE);
                        GlideUtils.loadGankRatioImage(AndroidFragment.this,
                                images + "?imageView2/0/w/500",
                                ((NormalHolder) holder).mImage);
                    } else {
                        ((NormalHolder) holder).mImage.setVisibility(View.GONE);
                    }
                } else {
                    ((NormalHolder) holder).mImage.setVisibility(View.GONE);
                }

            } else if (holder instanceof FooterHolder) {
                if (IsRefreshFinish) {
                    ((FooterHolder) holder).mProgress.setVisibility(View.GONE);
                    ((FooterHolder) holder).mText.setText(getResources().getString(R.string.load_finish));
                } else {
                    ((FooterHolder) holder).mProgress.setVisibility(View.VISIBLE);
                    ((FooterHolder) holder).mText.setText(getResources().getString(R.string.load_more));
                }
            }
        }


        @Override
        public int getItemCount() {
            return AndroidEntitys.size() + 1;
        }

        class NormalHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_android_title)
            TextView mTitle;
            @BindView(R.id.tv_android_user)
            TextView mUser;
            @BindView(R.id.tv_android_date)
            TextView mDate;
            @BindView(R.id.iv_android_image)
            RatioImageView mImage;

            public NormalHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                mImage.setOriginalSize(70,40);
            }
        }

        class FooterHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.pb_load)
            ProgressBar mProgress;
            @BindView(R.id.tv_load)
            TextView mText;

            public FooterHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
