package com.example.lzc.daliylife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.fragments.fuli.AndroidFragment;
import com.example.lzc.daliylife.fragments.fuli.FuLiFragment;
import com.example.lzc.daliylife.fragments.fuli.IOSFragment;
import com.example.lzc.daliylife.fragments.fuli.QianDuanFragment;
import com.example.lzc.daliylife.fragments.fuli.TuoZhanFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzc on 2016/12/5.
 */

public class NewsFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.news_viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    private NewsFragmentAdapter newsFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.news, null);
        mUnbinder = ButterKnife.bind(this, mRootView);
        newsFragmentAdapter = new NewsFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(newsFragmentAdapter);
        initTabs();
        return mRootView;
    }

    private void initTabs() {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class NewsFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> pagerTitles;

        public NewsFragmentAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(new FuLiFragment());
            fragments.add(new AndroidFragment());
            fragments.add(new IOSFragment());
            fragments.add(new QianDuanFragment());
            fragments.add(new TuoZhanFragment());
            pagerTitles = new ArrayList<>();
            pagerTitles.add("福利");
            pagerTitles.add("Android");
            pagerTitles.add("iOS");
            pagerTitles.add("前端");
            pagerTitles.add("拓展资源");
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pagerTitles.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
