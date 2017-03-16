package com.example.lzc.daliylife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzc on 2017/3/10.
 */

public class OtherFragment extends Fragment {
    private Unbinder mBind;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.laohuangli_fragment,null);
        mBind = ButterKnife.bind(this, viewRoot);
        mToolbar.setTitle(getResources().getString(R.string.toolbar_wechart));
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        //绑定DrawerLayout
        ((MainActivity)getActivity()).initDrawerLayout(mToolbar);
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }
}
