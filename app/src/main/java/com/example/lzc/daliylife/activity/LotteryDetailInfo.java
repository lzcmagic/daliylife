package com.example.lzc.daliylife.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.entity.LotteryEntity;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.normalUtil.DensityUtils;
import com.example.lzc.daliylife.views.RatioImageView;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzc on 2017/3/20.
 */

public class LotteryDetailInfo extends AppCompatActivity {
    private LotteryEntity CurrentLottery;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_detail);
        ButterKnife.bind(this);
        mToolbar.setTitle("彩票信息");
        setSupportActionBar(mToolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        parseIntent();
        initLotteryNumber();
        String sales = CurrentLottery.getResult().getSales();
        BigDecimal bd = new BigDecimal(sales);
        String pool = CurrentLottery.getResult().getPool();
        BigDecimal bdpool = new BigDecimal(pool);
        descText.setText(Html.fromHtml("<font color=\"#515151\">"+"本期销量:"+
                "</font>"+"<font color=\"#c62828\">"+bd.toPlainString()+"</font>"+
                "</br>"+
                "<font color=\"#515151\">"+"奖池滚存:"+"</font>"+
                "<font color=\"#c62828\">"+bdpool.toPlainString()+"</font>"));
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter() {
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
            View view1 = mInflater.inflate(R.layout.activity_lottery_detail_item, viewGroup, false);
            TextView textView = (TextView) view1.findViewById(R.id.tv_lottery_item);
            textView.setText(lotteryDetails.get(i));
            if (i == 0 || i == 1 || i == 2) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                textView.setBackgroundColor(0x99cccccc);
            }
            return view1;
        }
    }

    /**
     * 初始化彩票
     */
    private void initLotteryNumber() {
        LinearLayout.LayoutParams params = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = DensityUtils.dp2px(LotteryDetailInfo.this, 24f);
        params.height = DensityUtils.dp2px(LotteryDetailInfo.this, 24f);
        LotteryEntity.Result result = CurrentLottery.getResult();
        String name = result.getName();
        ArrayList<String> lotteryNumber =
                result.getLotteryNumber();
        LotteryTitle.setText(name);

        if (name.equals(Constants.LOTTERY_SSQ)) {
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
        } else if (name.equals(Constants.LOTTERY_DLT)) {
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
            mImage.setImageResource(R.mipmap.lottery_qlc);
            date.setText("20" + result.getPeriod() + "期");
        } else if (name.equals(Constants.LOTTERY_3D)) {
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
        } else if (name.equals(Constants.LOTTERY_PL3)) {
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
        } else if (name.equals(Constants.LOTTERY_PL5)) {
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
        } else if (name.equals(Constants.LOTTERY_QLC)) {
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
        } else if (name.equals(Constants.LOTTERY_QXC)) {
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
        }
    }

    private void parseIntent() {
        CurrentLottery = (LotteryEntity) getIntent().getSerializableExtra("lottery");
        lotteryDetails = new ArrayList<>();
        lotteryDetails.add("奖项");
        lotteryDetails.add("中奖注数");
        lotteryDetails.add("单注中奖(元)");
        ArrayList<LotteryEntity.Result.LotteryDetail> lotteryDetailss = CurrentLottery.getResult().getLotteryDetails();
        for (LotteryEntity.Result.LotteryDetail lotteryDetail : lotteryDetailss) {
            if (lotteryDetail.getType() != null) {
                lotteryDetails.add(lotteryDetail.getType() + lotteryDetail.getAwards());
            } else {
                lotteryDetails.add(lotteryDetail.getAwards());
            }
            lotteryDetails.add(lotteryDetail.getAwardNumber());
            lotteryDetails.add(lotteryDetail.getAwardPrice());
        }

    }

    public static void actionStart(Context content, LotteryEntity lotteryEntity) {
        Intent intent = new Intent(content, LotteryDetailInfo.class);
        intent.putExtra("lottery", lotteryEntity);
        content.startActivity(intent);
    }

}
