package com.lzc.daliylife.calender;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.base.BaseFragment;
import com.lzc.daliylife.entity.LaoHuangLiEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.http.HttpMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.lzc.daliylife.R.id.fab_select_date;

/**
 * Created by lzc on 2016/12/5.
 *
 */

public class DaliyEventsFragment extends BaseFragment {
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
    /**
     * 当前对话框显示的日期
     */
    public Calendar DateDefault = Calendar.getInstance();
    private DatePickerDialog mDatePickerDialog;
    boolean isFirstSelect=true;
    private ProgressDialog mProgressDialog;

    @Override
    public int getResId() {
        return R.layout.daliy;
    }

    @Override
    public void initUI() {
        initDatePickerDialog();
        initProgressDialog();
        mProgressDialog.show();
        initDate(null);
        initFab();
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

    private Disposable disposable;
    /**
     * 初始化数据
     */
    private void initDate(Calendar calendar) {
        String date;
        if (calendar == null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(new Date());
        } else {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            date = year +"-"+ (month > 9 ? month+"" : "0" + month) + "-"+(day > 9 ? "" + day : "0" + day);
        }
        HttpMethods.getInstance(Constants.LAOHUANGLIAPI)
                .getDayLHL(new Observer<LaoHuangLiEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(LaoHuangLiEntity laoHuangLiEntity) {
                        LaoHuangLiEntity.Result result = laoHuangLiEntity.getResult();
                        StringBuilder sb_avoid = new StringBuilder();
                        StringBuilder sb_suit = new StringBuilder();
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
                        mProgressDialog.dismiss();
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
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable!=null)
        disposable.dispose();
    }
}
