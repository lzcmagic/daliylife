package com.lzc.daliylife.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzc.daliylife.normalUtil.L;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzc on 2017/9/8.
 */

public abstract class BaseFragment extends Fragment {
    Unbinder bind;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("Fragment onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d("Fragment onStart");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.d("Fragment onCreateView");
        View rootView = inflater.inflate(getResId(), container, false);
        bind = ButterKnife.bind(this, rootView);
        initUI();
        return rootView;
    }

    public abstract int getResId();

    public abstract void initUI();

    @Override
    public void onResume() {
        L.d("Fragment onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        L.d("Fragment onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        L.d("Fragment onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        L.d("Fragment onDestroyView");
        super.onDestroyView();
        if (bind!=null){
            bind.unbind();
        }
    }

    @Override
    public void onDestroy() {
        L.d("Fragment onDestroy");
        super.onDestroy();
    }
}
