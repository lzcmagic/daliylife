package com.lzc.daliylife.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.activity.LotteryDetailInfo;
import com.lzc.daliylife.base.BaseFragment;
import com.lzc.daliylife.main.MainActivity;
import com.lzc.daliylife.entity.LotteryEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.normalUtil.DensityUtils;
import com.lzc.daliylife.adapter.OnRecyclerViewItemClickListener;
import com.lzc.daliylife.utils.HttpMethods;
import com.lzc.daliylife.views.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * Created by lzc on 2017/3/10.
 */

public class LotteryFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_lotttery)
    RecyclerView mRecyclerView;
    @BindView(R.id.spl_lottery)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    String lotteryType[] = new String[]{
            Constants.LOTTERY_SSQ,
            Constants.LOTTERY_DLT,
            Constants.LOTTERY_3D,
            Constants.LOTTERY_PL3,
            Constants.LOTTERY_PL5,
            Constants.LOTTERY_QLC,
            Constants.LOTTERY_QXC};
    private MyAdapter mAdapter;
    private Map<Integer, LotteryEntity> LotteryMaps;
    private int CurrentIndex = 0;
    private boolean IsXiaLaRefresh = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LotteryMaps = new HashMap<>();
    }


    @Override
    public int getResId() {
        return R.layout.lottery_fragment;
    }

    @Override
    public void initUI() {
        mToolbar.setTitle(getResources().getString(R.string.toolbar_lottery));
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        //绑定DrawerLayout
        ((MainActivity) getActivity()).initDrawerLayout(mToolbar);
        initSwipeRefreshLayout();
        mAdapter = new MyAdapter();
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                LotteryDetailInfo.actionStart(getActivity(), LotteryMaps.get(position));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        IsXiaLaRefresh = false;
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        LotteryMaps.clear();
        for (int i = 0; i < lotteryType.length; i++) {

            HttpMethods.getInstance(Constants.LOTTERYAPI).getLotteryInfo(new Subscriber<LotteryEntity>() {
                @Override
                public void onCompleted() {
                    CurrentIndex += 1;
                    //保持最后一次刷新adapter
                    if (CurrentIndex == lotteryType.length) {
                        if (IsXiaLaRefresh) {
                            mRefreshLayout.setRefreshing(false);
                        } else {
                            mRefreshLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mRefreshLayout.setRefreshing(false);
                                }
                            }, 1000);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    mRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onNext(LotteryEntity lotteryEntity) {
                    String name = lotteryEntity.getResult().getName();
                    if (name.equals(Constants.LOTTERY_SSQ)) {
                        LotteryMaps.put(0, lotteryEntity);
                    } else if (name.equals(Constants.LOTTERY_DLT)) {
                        LotteryMaps.put(1, lotteryEntity);
                    } else if (name.equals(Constants.LOTTERY_3D)) {
                        LotteryMaps.put(2, lotteryEntity);
                    } else if (name.equals(Constants.LOTTERY_PL3)) {
                        LotteryMaps.put(3, lotteryEntity);
                    } else if (name.equals(Constants.LOTTERY_PL5)) {
                        LotteryMaps.put(4, lotteryEntity);
                    } else if (name.equals(Constants.LOTTERY_QLC)) {
                        LotteryMaps.put(5, lotteryEntity);
                    } else if (name.equals(Constants.LOTTERY_QXC)) {
                        LotteryMaps.put(6, lotteryEntity);
                    }
                }
            }, Constants.WEATHERKEY, lotteryType[i]);
        }
    }

     class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private LayoutInflater mInflater;
        private OnRecyclerViewItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        public MyAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MyAdapter.MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.adapter_lottery, viewGroup, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyHolder viewHolder,int i) {
            LotteryEntity lotteryEntity = LotteryMaps.get(i);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(viewHolder, viewHolder.getAdapterPosition());
                }
            });
            LotteryEntity.Result result = lotteryEntity.getResult();
            ArrayList<String> lotteryNumber = result.getLotteryNumber();
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            viewHolder.mLayout.removeAllViews();
            params.width = DensityUtils.dp2px(getContext(), 24f);
            params.height = DensityUtils.dp2px(getContext(), 24f);
            String name = result.getName();
            viewHolder.mTitleText.setText(name);
            if (name.equals(Constants.LOTTERY_SSQ)) {
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(getContext());
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                    }
                    if (lotteryNumber.size() == j + 1) {
                        view.setTextColor(getResources().getColor(R.color.lotteryBlue));
                        view.setBackgroundResource(R.mipmap.lottery_blue);
                    } else {
                        view.setTextColor(getResources().getColor(R.color.lotteryRed));
                        view.setBackgroundResource(R.mipmap.lottery_red);
                    }
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    viewHolder.mLayout.addView(view);
                }
                viewHolder.mDateText.setText("每周二,四,日(21:30)开奖");
                viewHolder.mSubTitleText.setText(result.getPeriod() + "期");
            } else if (name.equals(Constants.LOTTERY_DLT)) {
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(getContext());
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                    }
                    if (lotteryNumber.size() - 1 == j ||
                            lotteryNumber.size() - 2 == j) {
                        view.setTextColor(getResources().getColor(R.color.lotteryBlue));
                        view.setBackgroundResource(R.mipmap.lottery_blue);
                    } else {
                        view.setTextColor(getResources().getColor(R.color.lotteryRed));
                        view.setBackgroundResource(R.mipmap.lottery_red);
                    }
                    view.setLayoutParams(params);
                    view.setText(lotteryNumber.get(j));
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    viewHolder.mLayout.addView(view);
                }
                viewHolder.mDateText.setText("每周一,三,六(22:00)开奖");
                viewHolder.mSubTitleText.setText("20" + result.getPeriod() + "期");
            } else if (name.equals(Constants.LOTTERY_3D)) {
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(getContext());
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                    }
                    view.setTextColor(getResources().getColor(R.color.lotteryRed));
                    view.setBackgroundResource(R.mipmap.lottery_red);
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    viewHolder.mLayout.addView(view);
                }
                viewHolder.mDateText.setText("每天21:15开奖");
                viewHolder.mSubTitleText.setText(result.getPeriod() + "期");
            } else if (name.equals(Constants.LOTTERY_PL3)) {
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(getContext());
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                    }
                    view.setTextColor(getResources().getColor(R.color.lotteryRed));
                    view.setBackgroundResource(R.mipmap.lottery_red);
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    viewHolder.mLayout.addView(view);
                }
                viewHolder.mDateText.setText("每天20:30开奖");
                viewHolder.mSubTitleText.setText("20" + result.getPeriod() + "期");
            } else if (name.equals(Constants.LOTTERY_PL5)) {
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(getContext());
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                    }
                    view.setTextColor(getResources().getColor(R.color.lotteryRed));
                    view.setBackgroundResource(R.mipmap.lottery_red);
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    viewHolder.mLayout.addView(view);
                }
                viewHolder.mDateText.setText("每天20:30开奖");
                viewHolder.mSubTitleText.setText("20" + result.getPeriod() + "期");
            } else if (name.equals(Constants.LOTTERY_QLC)) {
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(getContext());
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                    }
                    if (lotteryNumber.size() - 1 == j) {
                        view.setTextColor(getResources().getColor(R.color.lotteryBlue));
                        view.setBackgroundResource(R.mipmap.lottery_blue);
                    } else {
                        view.setTextColor(getResources().getColor(R.color.lotteryRed));
                        view.setBackgroundResource(R.mipmap.lottery_red);
                    }
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    viewHolder.mLayout.addView(view);
                }
                viewHolder.mDateText.setText("每周一,三,五(21:30)开奖");
                viewHolder.mSubTitleText.setText(result.getPeriod() + "期");
            } else if (name.equals(Constants.LOTTERY_QXC)) {
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(getContext());
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                    }
                    view.setTextColor(getResources().getColor(R.color.lotteryRed));
                    view.setBackgroundResource(R.mipmap.lottery_red);
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    viewHolder.mLayout.addView(view);
                }
                viewHolder.mDateText.setText("每周二,五,日(20:30)开奖");
                viewHolder.mSubTitleText.setText("20" + result.getPeriod() + "期");
            }
        }

        @Override
        public int getItemCount() {
            return LotteryMaps.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_lottery_title)
            TextView mTitleText;
            @BindView(R.id.tv_lottery_sub_title)
            TextView mSubTitleText;
            @BindView(R.id.tv_lottery_date)
            TextView mDateText;
            @BindView(R.id.ll_lottery_container)
            LinearLayout mLayout;

            public MyHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void initSwipeRefreshLayout() {
        mRefreshLayout.setScrollUpChild(mRecyclerView);
        mRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CurrentIndex = 0;
                IsXiaLaRefresh = true;
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                }, 1000);
            }


        });
    }

}
