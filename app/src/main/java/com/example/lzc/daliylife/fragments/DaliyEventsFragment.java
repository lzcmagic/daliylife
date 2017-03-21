package com.example.lzc.daliylife.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.activity.MainActivity;
import com.example.lzc.daliylife.entity.LaoHuangLiEntity;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.utils.HttpMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

import static com.example.lzc.daliylife.R.id.fab_select_date;

/**
 * Created by lzc on 2016/12/5.
 */

public class DaliyEventsFragment extends Fragment {
    View mRootView;
    Unbinder mUnbind;
    @BindView(R.id.tv_normal_date)
    TextView NormalDate;
    @BindView(R.id.tv_lunar_date)
    TextView LunarDate;
    @BindView(R.id.tv_suit)
    TextView SuitThing;
    @BindView(R.id.tv_avoid)
    TextView AvoidThing;
    @BindView(R.id.tv_jishen)
    TextView JiShen;
    @BindView(R.id.tv_xiongshen)
    TextView XiongShen;
    @BindView(fab_select_date)
    FloatingActionButton SelectDate;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    /**
     * 当前对话框显示的日期
     */
    public Calendar DateDefault = Calendar.getInstance();
    private DatePickerDialog mDatePickerDialog;
    boolean isFirstSelect=true;
    private ProgressDialog mProgressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.daliy, null);
        mUnbind = ButterKnife.bind(this, mRootView);
        initDatePickerDialog();
        initProgressDialog();
        mProgressDialog.show();
        initDate(null);
        initFab();
        mToolbar.setTitle(getResources().getString(R.string.toolbar_daliy));
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        //绑定DrawerLayout
        ((MainActivity)getActivity()).initDrawerLayout(mToolbar);
        return mRootView;
    }

    /**
     * 初始化等待框
     */
    private void initProgressDialog() {
        mProgressDialog=new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.load_more));
    }

    /**
     * 初始化fab按钮点击
     */
    private void initFab() {
        SelectDate.setImageResource(R.mipmap.calendarr);
        SelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstSelect=true;
                mDatePickerDialog.show();
            }
        });
    }

    /**
     * 加载数据
     * @param calendar
     */
    private void loadDate(Calendar calendar) {
        initDate(calendar);
    }

    /**
     * 初始化数据
     */
    private void initDate(Calendar calendar) {
        String date = "";
        if (calendar == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(new Date());
        } else {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            date = year +"-"+ (month > 9 ? month+"" : "0" + month) + "-"+(day > 9 ? "" + day : "0" + day);
        }
        HttpMethods.getInstance(Constants.LAOHUANGLIAPI)
                .getDayLHL(new Subscriber<LaoHuangLiEntity>() {

                    @Override
                    public void onCompleted() {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onNext(LaoHuangLiEntity laoHuangLiEntity) {
                        LaoHuangLiEntity.Result result = laoHuangLiEntity.getResult();
                        StringBuffer sb_avoid = new StringBuffer();
                        StringBuffer sb_suit = new StringBuffer();
                        String avoid = result.getAvoid();
                        String jishen = result.getJishen();
                        JiShen.setText(jishen);
                        String xiongshen = result.getXiongshen();
                        XiongShen.setText(xiongshen);
                        String date = result.getDate();
                        NormalDate.setText(date);
                        String lunar = result.getLunar();
                        LunarDate.setText(lunar);
                        String avoids[] = avoid.split(" ");
                        for (int i = 0; i < avoids.length; i++) {
                            if (i != 0) {
                                if (i % 2 == 0) {
                                    sb_avoid.append(avoids[i] + " ");
                                } else {
                                    sb_avoid.append(avoids[i] + "\n");
                                }
                            } else {
                                sb_avoid.append(avoids[i] + " ");
                            }
                        }
                        AvoidThing.setText(sb_avoid.toString());
                        String suit = result.getSuit();
                        String suits[] = suit.split(" ");
                        for (int i = 0; i < suits.length; i++) {
                            if (i != 0) {
                                if (i % 2 == 0) {
                                    sb_suit.append(suits[i] + " ");
                                } else {
                                    sb_suit.append(suits[i] + "\n");
                                }
                            } else {
                                sb_suit.append(suits[i] + " ");
                            }
                        }
                        SuitThing.setText(sb_suit.toString());
                    }
                }, Constants.LAOHUANGLIKEY, date);
    }

    private void initDatePickerDialog() {
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        final int monthOfYear = Calendar.getInstance().get(Calendar.MONTH)+1;
        final int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        DateDefault.clear();
        mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (isFirstSelect){
                    DateDefault.set(Calendar.YEAR, year);
                    DateDefault.set(Calendar.MONTH, monthOfYear);
                    DateDefault.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    loadDate(DateDefault);
                    isFirstSelect=false;
                }

            }
        }, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbind.unbind();
    }
}
