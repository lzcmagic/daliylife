package com.lzc.daliylife.gank;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lzc.daliylife.R;
import com.lzc.daliylife.base.BaseFragment;
import com.lzc.daliylife.gank.android.AndroidFragment;
import com.lzc.daliylife.gank.expand.TuoZhanFragment;
import com.lzc.daliylife.gank.ios.IOSFragment;
import com.lzc.daliylife.gank.web.QianDuanFragment;
import com.lzc.daliylife.gank.welfare.WelfareFragment;
import com.lzc.daliylife.normalUtil.NetUtils;
import com.lzc.daliylife.normalUtil.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lzc on 2016/12/5.
 *
 */

public class GankFragment extends BaseFragment {
    @BindView(R.id.news_viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    private NewsFragmentAdapter newsFragmentAdapter;
    @BindView(R.id.fab_top)
    FloatingActionButton mFabButton;
    private int CurrentPageNumber = 1;

    public static GankFragment newInstance() {

        Bundle args = new Bundle();

        GankFragment fragment = new GankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getResId() {
        return R.layout.fragment_gank;
    }

    @Override
    public void initUI() {
        newsFragmentAdapter = new NewsFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(newsFragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CurrentPageNumber = position + 1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initTabs();
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (CurrentPageNumber) {
                    case 1: {
                        Fragment item = newsFragmentAdapter.getItem(0);
                        ((WelfareFragment) item).ScrollToTop();
                        break;
                    }
                    case 2: {
                        Fragment item = newsFragmentAdapter.getItem(1);
                        ((AndroidFragment) item).ScrollToTop();
                        break;
                    }
                    case 3: {
                        Fragment item = newsFragmentAdapter.getItem(2);
                        ((IOSFragment) item).ScrollToTop();
                        break;
                    }
                    case 4: {
                        Fragment item = newsFragmentAdapter.getItem(3);
                        ((QianDuanFragment) item).ScrollToTop();
                        break;
                    }
                    case 5: {
                        Fragment item = newsFragmentAdapter.getItem(4);
                        ((TuoZhanFragment) item).ScrollToTop();
                        break;
                    }
                }

            }
        });

    }

    private void initTabs() {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class NewsFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> pagerTitles;

        NewsFragmentAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(new WelfareFragment());
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

}
