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
 * Created by lzc on 2017/3/21.
 */

public class AboutUs extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.tb_about_us)
    Toolbar mToolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_about_us, container, false);
        unbinder= ButterKnife.bind(this, view);
        mToolbar.setTitle(getResources().getString(R.string.toolbar_daliy));
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        //绑定DrawerLayout
        ((MainActivity)getActivity()).initDrawerLayout(mToolbar);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
