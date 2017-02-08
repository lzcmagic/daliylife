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
 * Created by lzc on 2017/2/7.
 */

public class SportNewsFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.sport_text)
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView= inflater.inflate(R.layout.sport_news_fragment,null);
        mUnbinder= ButterKnife.bind(this,rootView);
        textView.setText("体育新闻");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
