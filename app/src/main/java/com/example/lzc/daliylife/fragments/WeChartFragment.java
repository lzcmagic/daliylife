package com.example.lzc.daliylife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzc on 2016/12/5.
 */

public class WeChartFragment extends Fragment {
    public View mRootView;
    Unbinder mUnbinder;
    @BindView(R.id.wechart_text)
    TextView mTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.wechart, null);
        mUnbinder=ButterKnife.bind(this,mRootView);
        mTextView.setText("this is wechart");
        mToolbar.setTitle(getResources().getString(R.string.toolbar_wechart));
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        //绑定DrawerLayout
        ((MainActivity)getActivity()).initDrawerLayout(mToolbar);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
