package com.example.lzc.daliylife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.activity.MainActivity;
import com.example.lzc.daliylife.fragments.fuli.AndroidFragment;
import com.example.lzc.daliylife.fragments.fuli.FuLiFragment;
import com.example.lzc.daliylife.fragments.fuli.IOSFragment;
import com.example.lzc.daliylife.fragments.fuli.QianDuanFragment;
import com.example.lzc.daliylife.fragments.fuli.TuoZhanFragment;
import com.example.lzc.daliylife.framework.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.lzc.daliylife.R.id.toolbar;

/**
 * Created by lzc on 2016/12/5.
 */

public class GankFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.news_viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    private NewsFragmentAdapter newsFragmentAdapter;
    @BindView(toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab_top)
    FloatingActionButton mFabButton;
    private int CurrentPageNumber = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.news, null);
        mUnbinder = ButterKnife.bind(this, mRootView);
        mToolbar.setTitle(getResources().getString(R.string.toolbar_news));
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
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        //绑定DrawerLayout
        ((MainActivity) getActivity()).initDrawerLayout(mToolbar);
        initTabs();
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constants.NORMALTAG,CurrentPageNumber+"");
                switch (CurrentPageNumber) {
                    case 1: {
                        Log.d(Constants.NORMALTAG, "FuLiFragment");
                        Fragment item = newsFragmentAdapter.getItem(0);
                        ((FuLiFragment) item).ScrollToTop();
                        break;
                    }
                    case 2: {
                        Log.d(Constants.NORMALTAG, "AndroidFragment");
                        Fragment item = newsFragmentAdapter.getItem(1);
                        ((AndroidFragment) item).ScrollToTop();
                        break;
                    }
                    case 3: {
                        Log.d(Constants.NORMALTAG, "IOSFragment");
                        Fragment item = newsFragmentAdapter.getItem(2);
                        ((IOSFragment) item).ScrollToTop();
                        break;
                    }
                    case 4: {
                        Log.d(Constants.NORMALTAG, "QianDuanFragment");
                        Fragment item = newsFragmentAdapter.getItem(3);
                        ((QianDuanFragment) item).ScrollToTop();
                        break;
                    }
                    case 5: {
                        Log.d(Constants.NORMALTAG, "TuoZhanFragment");
                        Fragment item = newsFragmentAdapter.getItem(4);
                        ((TuoZhanFragment) item).ScrollToTop();
                        break;
                    }
                }

            }
        });
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
