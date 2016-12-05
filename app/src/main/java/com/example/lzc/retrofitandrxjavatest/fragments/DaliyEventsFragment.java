package com.example.lzc.retrofitandrxjavatest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lzc.retrofitandrxjavatest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzc on 2016/12/5.
 */

public class DaliyEventsFragment extends Fragment {
    View mRootView;
    @BindView(R.id.daliy_text)
    TextView mTextView;
    Unbinder mUnbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.daliy,null);
         mUnbinder= ButterKnife.bind(this, mRootView);
        mTextView.setText("this is daliy_event");
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
