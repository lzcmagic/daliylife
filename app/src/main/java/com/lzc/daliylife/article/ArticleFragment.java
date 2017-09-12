package com.lzc.daliylife.article;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.OnRecyclerViewItemClickListener;
import com.lzc.daliylife.base.BaseFragment;
import com.lzc.daliylife.entity.juhe.WechatEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.gank.GankDetailInfo;
import com.lzc.daliylife.http.HttpMethods;
import com.lzc.daliylife.normalUtil.T;
import com.lzc.daliylife.utils.DateTimeFormat;
import com.lzc.daliylife.utils.GlideUtils;
import com.lzc.daliylife.utils.TabsIntentUtil;
import com.lzc.daliylife.views.RatioImageView;
import com.lzc.daliylife.views.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lzc on 2016/12/5.
 */

public class ArticleFragment extends BaseFragment {
    @BindView(R.id.rv_wechat)
    RecyclerView mRecyclerView;
    private boolean IsRefreshFinish;
    private boolean IsDataRefresh;
    private int Number = 10;
    private int Page = 1;
    private ArticleFragment.MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    @BindView(R.id.spl_wechart)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    private int LastVisiblePosition;
    List<WechatEntity.Result.ResultDetail> WechatList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WechatList = new ArrayList<>();
    }


    @Override
    public int getResId() {
        return R.layout.wechart;
    }

    @Override
    public void initUI() {
        WechatList.clear();
        initRefreshLauyout();
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter();
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
                WechatEntity.Result.ResultDetail result = WechatList.get(position);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(getResources().getColor(R.color.colorAccent));
                builder.setShowTitle(true);
                builder.addDefaultShareMenuItem();
                CustomTabsIntent customTabsIntent = builder.build();
                ArrayList customTabsPackages = TabsIntentUtil.getCustomTabsPackages(getContext());
                if (customTabsPackages!=null&&customTabsPackages.size()>0){
                    customTabsIntent.launchUrl(getActivity(), Uri.parse(result.getUrl()));
                }else{
                    Intent intent = new Intent(getActivity(), GankDetailInfo.class);
                    intent.putExtra("url", result.getUrl());
                    startActivity(intent);
                }
            }
        });
        initData();
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
                WechatList.clear();
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

    /**
     * 加载更多数据
     */
    private void loadData() {
        initData();
    }

    private Disposable disposable;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable!=null){
            disposable.dispose();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        HttpMethods.getInstance(Constants.WECHATAI).getWechatInfo(new Observer<WechatEntity>() {

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
            public void onComplete() {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                IsDataRefresh = false;
                if (WechatList.size()%Number>0) {
                    //加载完毕，(正好加载到第十条)
                    IsRefreshFinish = true;
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(WechatEntity wechatEntity) {
                ArrayList<WechatEntity.Result.ResultDetail> list = wechatEntity.getResult().getList();
                if (list != null && list.size() > 0) {
                    for (WechatEntity.Result.ResultDetail detail : list) {
                        WechatList.add(detail);
                    }
                }
            }
        }, Page, Number, "json",Constants.WECHATKEY);
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
            ArticleFragment.MyAdapter.this.mOnitemClickListener = mOnitemClickListener;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == LOAD_MORE) {
                View view = mInflater.inflate(R.layout.load_more_layout, parent, false);
                ArticleFragment.MyAdapter.FooterHolder holder = new ArticleFragment.MyAdapter.FooterHolder(view);
                return holder;

            } else if (viewType == LOAD_FINISH) {
                View view = mInflater.inflate(R.layout.wechart_fragment, parent, false);
                ArticleFragment.MyAdapter.NormalHolder holder = new ArticleFragment.MyAdapter.NormalHolder(view);
                return holder;
            } else {
                View view = mInflater.inflate(R.layout.wechart_fragment, parent, false);
                ArticleFragment.MyAdapter.NormalHolder holder = new ArticleFragment.MyAdapter.NormalHolder(view);
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

            if (holder instanceof ArticleFragment.MyAdapter.NormalHolder) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnitemClickListener.onItemClick(holder, position);
                    }
                });
                WechatEntity.Result.ResultDetail result = WechatList.get(position);
                ((NormalHolder) holder).mTitle.setText(result.getTitle());
                ((NormalHolder) holder).mSourece.setText("来源: " + result.getSource());
                String date = result.getId().split("_")[1];
                ((NormalHolder) holder).mDate.setText(DateTimeFormat.formatDateTime2(date));
                String firstImg = result.getFirstImg();
                GlideUtils.loadGankRatioImage(ArticleFragment.this,
                        firstImg, ((NormalHolder) holder).mImage);

            } else if (holder instanceof ArticleFragment.MyAdapter.FooterHolder) {
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
            return WechatList.size() + 1;
        }

        class NormalHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_wechart_title)
            TextView mTitle;
            @BindView(R.id.tv_wechart_source)
            TextView mSourece;
            @BindView(R.id.tv_wechart_date)
            TextView mDate;
            @BindView(R.id.iv_wechart_image)
            RatioImageView mImage;

            public NormalHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                //比例7:4
                mImage.setOriginalSize(70, 40);
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

}
