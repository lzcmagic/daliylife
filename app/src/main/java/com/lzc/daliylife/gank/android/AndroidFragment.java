package com.lzc.daliylife.gank.android;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.OnRecyclerScrolledListener;
import com.lzc.daliylife.adapter.OnRecyclerViewItemClickListener;
import com.lzc.daliylife.base.BaseFragment;
import com.lzc.daliylife.entity.gankentity.AndroidEntity;
import com.lzc.daliylife.entity.gankentity.Result;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.gank.GankDetailInfo;
import com.lzc.daliylife.gank.NormalAdapter;
import com.lzc.daliylife.http.HttpMethods;
import com.lzc.daliylife.normalUtil.T;
import com.lzc.daliylife.utils.TabsIntentUtil;
import com.lzc.daliylife.views.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lzc on 2017/2/7.
 *
 */

public class AndroidFragment extends BaseFragment {
    @BindView(R.id.spl_refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_android)
    RecyclerView mRecyclerView;
    private List<Result> AndroidEntitys;
    private boolean IsDataRefresh;
    private int Number = 10;
    private int Page = 1;
    private NormalAdapter mNormalAdapter;
    private boolean IsFirstLoad = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidEntitys = new ArrayList<>();
    }

    @Override
    public int getResId() {
        return R.layout.android_info_fragment;
    }

    @Override
    public void initUI() {
        //注意顺序
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mNormalAdapter =new NormalAdapter(getContext());
        mNormalAdapter.setLoadMoreRecyclerView(new OnRecyclerScrolledListener() {
            @Override
            public void onLoadMoreRequested() {
                if (IsDataRefresh){
                    return;
                }
                loadData();
            }
        },mRecyclerView);
        mNormalAdapter.setmOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                Result result = AndroidEntitys.get(position);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(getResources().getColor(R.color.colorAccent));
                builder.setShowTitle(true);
                builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_icon));
                builder.addDefaultShareMenuItem();
                CustomTabsIntent customTabsIntent = builder.build();
                ArrayList customTabsPackages = TabsIntentUtil.getCustomTabsPackages(getContext());
                if (customTabsPackages!=null&&customTabsPackages.size()>0){
                    customTabsIntent.launchUrl(getActivity(), Uri.parse(result.getUrl()));
                }else{
                    Intent intent = new Intent(getActivity(), GankDetailInfo.class);
                    intent.putExtra("url", result.getUrl());
                    intent.putExtra("title",result.getDesc());
                    startActivity(intent);
                }

            }
        });
        mNormalAdapter.setData(AndroidEntitys);

        mRecyclerView.setAdapter(mNormalAdapter);
        mNormalAdapter.notifyDataSetChanged();
        initRefreshLayout();
    }

    /**
     * 初始化下拉刷新控件
     */
    @SuppressWarnings("deprecation")
    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_blue_dark));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (IsDataRefresh)
                    return;

                Page = 1;
                IsDataRefresh = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //初始化
                        AndroidEntitys.clear();
                        //加载数据
                        initData();
                    }
                }, 500);

            }
        });
    }

    public void ScrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    /**
     * 加载更多数据
     */
    private void loadData() {
        Page++;
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
        HttpMethods.getInstance(Constants.GANKMAINAPI).getAndroidInfo(new Observer<AndroidEntity>() {

            @Override
            public void onError(Throwable e) {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                T.showShort(getString(R.string.load_data_abnormal));
                IsDataRefresh = false;
                mNormalAdapter.stopLoadData();
                mNormalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                IsDataRefresh = false;
                if (AndroidEntitys.size() < Number) {
                    //加载完毕，(正好加载到第十条)
                    mNormalAdapter.stopLoadData();
                }
                mNormalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
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
}

