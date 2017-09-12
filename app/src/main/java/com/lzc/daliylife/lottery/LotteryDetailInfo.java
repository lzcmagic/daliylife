package com.lzc.daliylife.lottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.entity.mob.LotteryEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.http.HttpMethods;
import com.lzc.daliylife.normalUtil.DensityUtils;
import com.lzc.daliylife.normalUtil.L;
import com.lzc.daliylife.normalUtil.T;
import com.lzc.daliylife.views.RatioImageView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lzc on 2017/3/20.
 *
 */

public class LotteryDetailInfo extends AppCompatActivity {
    @BindView(R.id.tb_lottery_detail)
    Toolbar mToolbar;
    @BindView(R.id.iv_lottery_detail_image)
    RatioImageView mImage;
    @BindView(R.id.tv_lottery_detail_title)
    TextView LotteryTitle;
    @BindView(R.id.tv_lottery_detail_date)
    TextView date;
    @BindView(R.id.ll_lottery_detail_container)
    LinearLayout mContainer;
    @BindView(R.id.gv_lottery_detail)
    GridView mGirdView;
    @BindView(R.id.tv_lottery_desc)
    TextView descText;
    private MyAdapter mAdapter;
    private List<String> lotteryDetails;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_detail);
        ButterKnife.bind(this);
        mToolbar.setTitle("彩票信息");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        parseIntent();


        mAdapter = new MyAdapter();
        mGirdView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }


    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        MyAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return lotteryDetails.size();
        }

        @Override
        public Object getItem(int i) {
            return lotteryDetails.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder") View view1 = mInflater.inflate(R.layout.activity_lottery_detail_item, viewGroup, false);
            TextView textView = (TextView) view1.findViewById(R.id.tv_lottery_item);
            textView.setText(lotteryDetails.get(i));
            if (i == 0 || i == 1 || i == 2) {
                textView.setBackgroundColor(0x99cccccc);
            }
            return view1;
        }
    }

    /**
     * 初始化彩票
     */
    @SuppressWarnings("deprecation")
    private void initLotteryNumber(LotteryEntity lotteryEntity) {
        LinearLayout.LayoutParams params = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = DensityUtils.dp2px(LotteryDetailInfo.this, 24f);
        params.height = DensityUtils.dp2px(LotteryDetailInfo.this, 24f);
        LotteryEntity.Result result = lotteryEntity.getResult();
        String name = result.getName();
        ArrayList<String> lotteryNumber =
                result.getLotteryNumber();
        LotteryTitle.setText(name);

        if (name.equals("3D")){
            name=Constants.LOTTERY_3D;
        }
        if (name.equals("大乐透")){
            name=Constants.LOTTERY_DLT;
        }
        switch (name){
            case Constants.LOTTERY_SSQ:
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(LotteryDetailInfo.this);
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(LotteryDetailInfo.this, 5f);
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
                    mContainer.addView(view);
                }
                mImage.setImageResource(R.mipmap.lottery_ssq);
                date.setText(result.getPeriod() + "期");
                break;
            case Constants.LOTTERY_DLT:
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(LotteryDetailInfo.this);
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(LotteryDetailInfo.this, 5f);
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
                    mContainer.addView(view);
                }
                mImage.setImageResource(R.mipmap.lottery_dlt);
                date.setText("20" + result.getPeriod() + "期");
                break;
            case Constants.LOTTERY_3D:
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(LotteryDetailInfo.this);
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(LotteryDetailInfo.this, 5f);
                    }
                    view.setTextColor(getResources().getColor(R.color.lotteryRed));
                    view.setBackgroundResource(R.mipmap.lottery_red);
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    mContainer.addView(view);
                }
                mImage.setImageResource(R.mipmap.lottery_fc);
                date.setText(result.getPeriod() + "期");
                break;
            case Constants.LOTTERY_PL3:
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(LotteryDetailInfo.this);
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(LotteryDetailInfo.this, 5f);
                    }
                    view.setTextColor(getResources().getColor(R.color.lotteryRed));
                    view.setBackgroundResource(R.mipmap.lottery_red);
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    mContainer.addView(view);
                }
                mImage.setImageResource(R.mipmap.lottery_pl3);
                date.setText("20" + result.getPeriod() + "期");
                break;
            case Constants.LOTTERY_PL5:
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(LotteryDetailInfo.this);
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(LotteryDetailInfo.this, 5f);
                    }
                    view.setTextColor(getResources().getColor(R.color.lotteryRed));
                    view.setBackgroundResource(R.mipmap.lottery_red);
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    mContainer.addView(view);
                }
                mImage.setImageResource(R.mipmap.lottery_pl3);
                date.setText("20" + result.getPeriod() + "期");
                break;
            case Constants.LOTTERY_QLC:
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(LotteryDetailInfo.this);
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(LotteryDetailInfo.this, 5f);
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
                    mContainer.addView(view);
                }
                mImage.setImageResource(R.mipmap.lottery_qlc);
                date.setText(result.getPeriod() + "期");
                break;
            case Constants.LOTTERY_QXC:
                for (int j = 0; j < lotteryNumber.size(); j++) {
                    TextView view = new TextView(LotteryDetailInfo.this);
                    if (j != 0) {
                        params.leftMargin = DensityUtils.dp2px(LotteryDetailInfo.this, 5f);
                    }
                    view.setTextColor(getResources().getColor(R.color.lotteryRed));
                    view.setBackgroundResource(R.mipmap.lottery_red);
                    view.setLayoutParams(params);
                    view.setTypeface(Typeface.MONOSPACE);
                    view.setTextSize(14f);
                    view.setGravity(Gravity.CENTER);
                    view.setText(lotteryNumber.get(j));
                    mContainer.addView(view);
                }
                mImage.setImageResource(R.mipmap.lottery_qxc);
                date.setText("20" + result.getPeriod() + "期");
                break;
            default:{
                break;
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void parseIntent() {
        String lotteryType = getIntent().getStringExtra("lotteryType");
        if(TextUtils.isEmpty(lotteryType)){
            T.toast("未知异常，请稍后再试");
            return;
        }
        lotteryDetails = new ArrayList<>();
        lotteryDetails.add("奖项");
        lotteryDetails.add("中奖注数");
        lotteryDetails.add("单注中奖(元)");

        initData(lotteryType);
    }

    @SuppressWarnings("deprecation")
    private void initData(String lotteryType) {

        if (lotteryType.equals(Constants.LOTTERY_3D)){
            lotteryType="3D";
        }
        if (lotteryType.equals(Constants.LOTTERY_DLT)){
            lotteryType="大乐透";
        }
        L.d("lotteryType: "+lotteryType);
        HttpMethods.getInstance(Constants.LOTTERYAPI).getLotteryInfo(new Observer<LotteryEntity>() {

            @Override
            public void onError(Throwable e) {
                T.toast("数据获取失败，请稍后再试");
            }

            @Override
            public void onComplete() {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }


            @Override
            public void onNext(LotteryEntity lotteryEntity) {
                if (!lotteryEntity.getRetCode().equals("200")){
                    T.toast("服务器内部异常");
                    return;
                }
                ArrayList<LotteryEntity.Result.LotteryDetail> lotteryDetailss = lotteryEntity.getResult().getLotteryDetails();
                for (LotteryEntity.Result.LotteryDetail lotteryDetail : lotteryDetailss) {
                    if (lotteryDetail.getType() != null) {
                        lotteryDetails.add(lotteryDetail.getType() + lotteryDetail.getAwards());
                    } else {
                        lotteryDetails.add(lotteryDetail.getAwards());
                    }
                    lotteryDetails.add(lotteryDetail.getAwardNumber());
                    lotteryDetails.add(lotteryDetail.getAwardPrice());
                }

                String sales = lotteryEntity.getResult().getSales();
                BigDecimal bd = new BigDecimal(sales);
                String pool = lotteryEntity.getResult().getPool();
                BigDecimal bdpool = new BigDecimal(pool);
                descText.setText(Html.fromHtml("<font color=\"#515151\">" + "本期销量: " +
                        "</font>" + "<font color=\"#c62828\">" + bd.toPlainString() + "</font>" +
                        "</br>" +
                        "<font color=\"#515151\">" + " 奖池滚存: " + "</font>" +
                        "<font color=\"#c62828\">" + bdpool.toPlainString() + "</font>"));
                initLotteryNumber(lotteryEntity);

            }
        }, Constants.WEATHERKEY, lotteryType);
    }

    public static void actionStart(Context content, String lotteryType) {
        Intent intent = new Intent(content, LotteryDetailInfo.class);
        intent.putExtra("lotteryType", lotteryType);
        content.startActivity(intent);
    }

}
