package com.lzc.daliylife.gank.welfare;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.OnRecyclerScrolledListener;
import com.lzc.daliylife.adapter.OnRecyclerViewItemClickListener;
import com.lzc.daliylife.base.BaseFragment;
import com.lzc.daliylife.entity.gankentity.FuLiEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.http.HttpMethods;
import com.lzc.daliylife.normalUtil.T;
import com.lzc.daliylife.utils.DateTimeFormat;
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

public class WelfareFragment extends BaseFragment {
    @BindView(R.id.hot_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.spl_refresh)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    private WelfareAdapter mWelfareAdapter;
    private List<FuLiEntity.Result> FuLiLists;

    private int Number = 10;
    //从1开始
    private int Page = 1;
    private boolean IsDataRefresh = false;
    private boolean IsFirstLoad = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuLiLists = new ArrayList<>();
    }

    @Override
    public int getResId() {
        return R.layout.gank_welfare_fragment;
    }

    @Override
    public void initUI() {
        StaggeredGridLayoutManager mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);

        mWelfareAdapter =new WelfareAdapter(getContext());
        mWelfareAdapter.setLoadMoreRecyclerView(new OnRecyclerScrolledListener() {
            @Override
            public void onLoadMoreRequested() {
                if (IsDataRefresh){
                    return;
                }
                loadData();
            }
        },recyclerView);
        mWelfareAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                Intent intent = WelfareActivity.newIntent(getContext(),
                        FuLiLists.get(position).getUrl(),
                        DateTimeFormat.formatDateTime(FuLiLists.get(position).getPublishedAt()));
                WelfareAdapter.FuliHolder viewById = (WelfareAdapter.FuliHolder) holder;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(),
                                viewById.fuliImage,
                                WelfareActivity.TRANSIT_PIC);

                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
        mWelfareAdapter.setData(FuLiLists);
        recyclerView.setAdapter(mWelfareAdapter);
        mWelfareAdapter.notifyDataSetChanged();
        initSwipeLayout();
    }


    public void ScrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    /**
     * 初始化下拉刷新
     */
    @SuppressWarnings("deprecation")
    private void initSwipeLayout() {
        mRefreshLayout.setScrollUpChild(recyclerView);
        mRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_blue_dark));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!IsDataRefresh) {
                    Page = 1;
                    IsDataRefresh = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FuLiLists.clear();
                            //加载数据
                            loadData();
                        }
                    }, 500);
                }

            }


        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && IsFirstLoad) {
            initFuLiData();
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
     * 加载数据
     */
    private void loadData() {
        Page++;
        initFuLiData();
    }

    /**
     * 初始化福利图片
     */
    private void initFuLiData() {
        HttpMethods.getInstance(Constants.GANKMAINAPI).getFuLiInfo(new Observer<FuLiEntity>() {


            @Override
            public void onError(Throwable e) {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                T.showShort(getString(R.string.load_data_abnormal));
                IsDataRefresh = false;
                mWelfareAdapter.stopLoadData();
                mWelfareAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                IsDataRefresh = false;
                Log.d("lzc","size: "+FuLiLists.size());
                if (FuLiLists.size()<Number) {
                    //加载完毕，(正好加载到第十条)
                    mWelfareAdapter.stopLoadData();
                }
                mWelfareAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
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
}
