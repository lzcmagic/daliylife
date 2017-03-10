package com.example.lzc.daliylife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.entity.LaoHuangLiEntity;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.utils.HttpMethods;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * Created by lzc on 2016/12/5.
 */

public class DaliyEventsFragment extends Fragment {
    View mRootView;
    @BindView(R.id.daliy_text)
    TextView mTextView;
    Unbinder mUnbind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.daliy, null);
        mUnbind = ButterKnife.bind(this, mRootView);
        mTextView.setText("this is daliy_event");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        HttpMethods.getInstance(Constants.LAOHUANGLIAPI)
                .getDayLHL(new Subscriber<LaoHuangLiEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LaoHuangLiEntity laoHuangLiEntity) {
                        mTextView.setText(laoHuangLiEntity.toString());
                    }
                },Constants.LAOHUANGLIKEY,date);
        return mRootView;
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
