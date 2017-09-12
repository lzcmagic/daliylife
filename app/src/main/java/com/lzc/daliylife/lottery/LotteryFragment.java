package com.lzc.daliylife.lottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.OnRecyclerViewItemClickListener;
import com.lzc.daliylife.base.BaseFragment;
import com.lzc.daliylife.entity.SummaryLotteryEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.http.HttpMethods;
import com.lzc.daliylife.normalUtil.DensityUtils;
import com.lzc.daliylife.normalUtil.T;
import com.lzc.daliylife.utils.SignUtil;
import com.lzc.daliylife.views.ScrollChildSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lzc on 2017/3/10.
 *
 */

public class LotteryFragment extends BaseFragment {
    @BindView(R.id.rv_lotttery)
    RecyclerView mRecyclerView;
    @BindView(R.id.spl_lottery)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    private MyAdapter mAdapter;
    private boolean IsXiaLaRefresh = false;
    private List<SummaryLotteryEntity.ShowapiResBodyBean.ResultBean> lotteryEntitys;
    private Disposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lotteryEntitys=new ArrayList<>();
    }


    @Override
    public int getResId() {
        return R.layout.lottery_fragment;
    }

    @Override
    public void initUI() {
        initSwipeRefreshLayout();
        mAdapter = new MyAdapter();
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                LotteryDetailInfo.actionStart(getActivity(), lotteryEntitys.get(position).getName());
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
    @SuppressLint("SimpleDateFormat")
    private void initData() {
        lotteryEntitys.clear();
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String temp="codessq|dlt|fc3d|pl3|pl5|qlc|qxcshowapi_appid"+Constants.YIYUANAPPID+"showapi_res_gzip0showapi_sign_methodmd5showapi_timestamp"+time;
        String sign = SignUtil.YiYuanSign(temp);
        Map<String,String> map=new HashMap<>();
        map.put("code","ssq|dlt|fc3d|pl3|pl5|qlc|qxc");
        map.put("showapi_appid",Constants.YIYUANAPPID);
        map.put("showapi_timestamp",time);
        map.put("showapi_sign_method","md5");
        map.put("showapi_res_gzip","0");
        map.put("showapi_sign",sign);

        HttpMethods.getInstance(Constants.YIYUANADDRESS).getYYLotteryInfo(new Observer<SummaryLotteryEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(SummaryLotteryEntity value) {
                int ret_code = value.getShowapi_res_body().getRet_code();
                if (ret_code==0){
                    lotteryEntitys= value.getShowapi_res_body().getResult();
                }
            }

            @Override
            public void onError(Throwable e) {
                mRefreshLayout.setRefreshing(false);
                T.toast("数据加载失败");
            }

            @Override
            public void onComplete() {
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
        },"44-1",map);
    }

     class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private LayoutInflater mInflater;
        private OnRecyclerViewItemClickListener mOnItemClickListener;

        void setOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        MyAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MyAdapter.MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.adapter_lottery, viewGroup, false);
            return new MyHolder(view);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onBindViewHolder(final MyAdapter.MyHolder viewHolder,int i) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(viewHolder, viewHolder.getAdapterPosition());
                }
            });

            SummaryLotteryEntity.ShowapiResBodyBean.ResultBean resultBean = lotteryEntitys.get(i);

            String openCode = resultBean.getOpenCode().replace("+",",").replace(" ","");
            String[] lotteryNumber = openCode.split(",");
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            viewHolder.mLayout.removeAllViews();
            params.width = DensityUtils.dp2px(getContext(), 24f);
            params.height = DensityUtils.dp2px(getContext(), 24f);
            String name = resultBean.getName();
            viewHolder.mTitleText.setText(name);
            viewHolder.mSubTitleText.setText( resultBean.getExpect() + "期");
            switch (name){
                case Constants.LOTTERY_SSQ:
                    for (int j = 0; j < lotteryNumber.length; j++) {
                        TextView view = new TextView(getContext());
                        if (j != 0) {
                            params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                        }
                        if (lotteryNumber.length == j + 1) {
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
                        view.setText(lotteryNumber[j]);
                        viewHolder.mLayout.addView(view);
                    }
                    viewHolder.mDateText.setText("每周二,四,日(21:30)开奖");
                    break;
                case Constants.LOTTERY_DLT:
                    for (int j = 0; j < lotteryNumber.length; j++) {
                        TextView view = new TextView(getContext());
                        if (j != 0) {
                            params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                        }
                        if (lotteryNumber.length - 1 == j ||
                                lotteryNumber.length - 2 == j) {
                            view.setTextColor(getResources().getColor(R.color.lotteryBlue));
                            view.setBackgroundResource(R.mipmap.lottery_blue);
                        } else {
                            view.setTextColor(getResources().getColor(R.color.lotteryRed));
                            view.setBackgroundResource(R.mipmap.lottery_red);
                        }
                        view.setLayoutParams(params);
                        view.setText(lotteryNumber[j]);
                        view.setTypeface(Typeface.MONOSPACE);
                        view.setTextSize(14f);
                        view.setGravity(Gravity.CENTER);
                        viewHolder.mLayout.addView(view);
                    }
                    viewHolder.mDateText.setText("每周一,三,六(22:00)开奖");
                    break;
                case Constants.LOTTERY_3D:
                    for (int j = 0; j < lotteryNumber.length; j++) {
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
                        view.setText(lotteryNumber[j]);
                        viewHolder.mLayout.addView(view);
                    }
                    viewHolder.mDateText.setText("每天21:15开奖");
                    break;
                case Constants.LOTTERY_PL3:
                    for (int j = 0; j < lotteryNumber.length; j++) {
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
                        view.setText(lotteryNumber[j]);
                        viewHolder.mLayout.addView(view);
                    }
                    viewHolder.mDateText.setText("每天20:30开奖");
                    break;
                case Constants.LOTTERY_PL5:
                    for (int j = 0; j < lotteryNumber.length; j++) {
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
                        view.setText(lotteryNumber[j]);
                        viewHolder.mLayout.addView(view);
                    }
                    viewHolder.mDateText.setText("每天20:30开奖");
                    break;
                case Constants.LOTTERY_QLC:
                    for (int j = 0; j < lotteryNumber.length; j++) {
                        TextView view = new TextView(getContext());
                        if (j != 0) {
                            params.leftMargin = DensityUtils.dp2px(getContext(), 5f);
                        }
                        if (lotteryNumber.length - 1 == j) {
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
                        view.setText(lotteryNumber[j]);
                        viewHolder.mLayout.addView(view);
                    }
                    viewHolder.mDateText.setText("每周一,三,五(21:30)开奖");
                    break;
                case Constants.LOTTERY_QXC:
                    for (int j = 0; j < lotteryNumber.length; j++) {
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
                        view.setText(lotteryNumber[j]);
                        viewHolder.mLayout.addView(view);
                    }
                    viewHolder.mDateText.setText("每周二,五,日(20:30)开奖");
                    break;
                default:{
                    break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return lotteryEntitys.size();
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

            MyHolder(View itemView) {
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
//                CurrentIndex = 0;
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
