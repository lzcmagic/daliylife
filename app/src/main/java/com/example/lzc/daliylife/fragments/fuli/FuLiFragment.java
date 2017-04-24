package com.example.lzc.daliylife.fragments.fuli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.activity.FuLiDetailActivity;
import com.example.lzc.daliylife.entity.gankentity.FuLiEntity;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.normalUtil.T;
import com.example.lzc.daliylife.utillistener.OnRecyclerViewItemClickListener;
import com.example.lzc.daliylife.utils.DateTimeFormat;
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
//    private LinearLayoutManager mLinearManager;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private int LastVisiblePosition;
    private boolean IsRefreshFinish = false;
    private boolean IsDataRefresh = false;
    private boolean IsFirstLoad = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuLiLists = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hot_news_fragment, null);
        unBinder = ButterKnife.bind(this, rootView);
        initSwipeLayout();
//        mLinearManager = new LinearLayoutManager(getContext());
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        mFuLiAdapter = new FuLiAdapter();
        recyclerView.setAdapter(mFuLiAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = recyclerView.getVerticalScrollbarPosition();
                Log.d("lzc","refresh: "+IsRefreshFinish);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        mFuLiAdapter.getItemCount() == LastVisiblePosition + 1 &&
                        !IsRefreshFinish) {
                    if (IsDataRefresh)
                        return;
                    //执行刷新
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
                int[] into=new int[mStaggeredLayoutManager.getSpanCount()];
                 mStaggeredLayoutManager.findLastVisibleItemPositions(into);
                LastVisiblePosition =findMax(into);
            }
        });
        mFuLiAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                Intent intent = FuLiDetailActivity.newIntent(getContext(),
                        FuLiLists.get(position).getUrl(),
                        DateTimeFormat.formatDateTime(FuLiLists.get(position).getPublishedAt()));
                FuLiAdapter.MyHolder viewById = (FuLiAdapter.MyHolder) holder;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(),
                                viewById.fuliImage,
                                FuLiDetailActivity.TRANSIT_PIC);

                if (android.os.Build.VERSION.SDK_INT > 20) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
        return rootView;
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

    public void ScrollToTop() {
        recyclerView.scrollToPosition(0);
    }

    private void cache(){

    }

    /**
     * 初始化下拉刷新
     */
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
                    FuLiLists.clear();
                    IsDataRefresh = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                Log.d("lzc","size: "+FuLiLists.size());
                if (FuLiLists.size()%Number>0) {
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

    class FuLiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater mInflater;
        private int LOAD_MORE = 1;
        private int LOAD_FINISH = 2;
        private OnRecyclerViewItemClickListener mOnItemClickListener;

        public FuLiAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
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
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof MyHolder) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder, position);
                    }
                });
                ((MyHolder) holder).fuliText.setText(FuLiLists.get(position).getDesc());
                Glide.with(FuLiFragment.this)
                        //加载500像素的图片
                        .load(FuLiLists.get(position).getUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        //.placeholder(R.mipmap.loading)
                        .centerCrop()
                        .error(R.mipmap.fail_load)
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


        class MyHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_fuli_desc)
            TextView fuliText;
            @BindView(R.id.iv_fuli_image)
            RatioImageView fuliImage;

            public MyHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                //4:5
                fuliImage.setOriginalSize(40, 50);
//                ButterKnife.bind(this,itemView);
            }
        }

        class LoadMoreHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.pb_load)
             ProgressBar mProgress;
            @BindView(R.id.tv_load)
             TextView mText;

            public LoadMoreHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();
    }
}
