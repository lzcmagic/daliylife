package com.lzc.daliylife.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzc.daliylife.R;
import com.lzc.daliylife.base.BaseActivity;
import com.lzc.daliylife.entity.LocationEntity;
import com.lzc.daliylife.entity.WeatherEntity;
import com.lzc.daliylife.fragments.AboutUs;
import com.lzc.daliylife.fragments.DaliyEventsFragment;
import com.lzc.daliylife.fragments.GankFragment;
import com.lzc.daliylife.fragments.OtherFragment;
import com.lzc.daliylife.fragments.WeChartFragment;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.utils.AMapUtils;
import com.lzc.daliylife.utils.ActivityUtils;
import com.lzc.daliylife.utils.HttpMethods;
import com.lzc.daliylife.utils.WeatherToIcon;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import butterknife.BindView;
import rx.Subscriber;

/**
 * 主页
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainContract.MView {
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ImageView weatherIconImg;

    private ProgressDialog mPDialog;

    private ShareAction mShareAction;

    private MainPresenter mainPresenter;

    @Override
    protected void onStart() {
        super.onStart();
        GankFragment gankFragment =
                (GankFragment) getSupportFragmentManager().findFragmentById(R.id.frame_cont);
        if (gankFragment==null){
            gankFragment=GankFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    gankFragment, R.id.frame_cont);
        }

    }

    @Override
    public void initUI() {
        /*增加自定义按钮的分享面板*/
        mainPresenter.initWeather();
        mShareAction = new CustomerShareAction(this);
        initProgressDialog();
        navView.setNavigationItemSelectedListener(this);
        ColorStateList stateList = getResources().getColorStateList(R.color.navigation_menu_item_color);
        navView.setItemIconTintList(stateList);
        navView.setItemTextColor(stateList);
        navView.getMenu().getItem(0).setChecked(true);
        weatherIconImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.refreshWeather();
            }
        });
    }

    @Override
    public void initPresenter() {
        mainPresenter = new MainPresenter(this);
        mainPresenter.attachView(this);
    }

    @Override
    public int getResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void showWeather(String weather, String weatherText, String temperature) {
        View headerView = navView.getHeaderView(0);
        weatherIconImg = (ImageView) headerView.findViewById(R.id.imageView);
        final TextView weatherView = (TextView) headerView.findViewById(R.id.weather);
        final TextView weatherTextView = (TextView) headerView.findViewById(R.id.weather_text);
        final TextView weatherTemp = (TextView) headerView.findViewById(R.id.weather_temp);
        if (!TextUtils.isEmpty(weather)) {
            weatherView.setText(weather);
            weatherIconImg.setImageResource(WeatherToIcon.Weather2Icon(weather));
        } else {
            //获取天气信息失败
            weatherView.setText("天气信息获取失败,点击图标重试");
        }
        if (!TextUtils.isEmpty(weatherText)) {
            weatherTextView.setText(weatherText);
        } else {
            //获取天气信息失败
            weatherTextView.setText("");
        }
        if (!TextUtils.isEmpty(temperature)) {
            weatherTemp.setText(temperature);
        } else {
            //获取天气信息失败
            weatherTemp.setText("");
        }

    }

    @Override
    public void showDialog() {
        if (mPDialog != null) {
            mPDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (mPDialog != null && mPDialog.isShowing()) {
            mPDialog.hide();
        }
    }

    /**
     * 初始化抽屉
     *
     * @param mToolbar toolbar
     */
    public void initDrawerLayout(final Toolbar mToolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initProgressDialog() {
        mPDialog = new ProgressDialog(this);
        mPDialog.setMessage("正在加载中...");
    }

    /**
     * intent
     *
     * @param context
     */
    public static void actionStart(Context context, String weather, String weatherText, String temperature) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("weather", weather);
        intent.putExtra("weatherText", weatherText);
        intent.putExtra("temperature", temperature);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction mTransaction =
                getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.news:
                mTransaction.replace(R.id.frame_cont, new GankFragment(), Constants.FragmentTagNews);
                break;
            case R.id.wechart:
                mTransaction.replace(R.id.frame_cont, new WeChartFragment(), Constants.FragmentTagWeChart);
                break;
            case R.id.car:
                mTransaction.replace(R.id.frame_cont, new OtherFragment(), Constants.FragmentTagLottery);
                break;
            case R.id.daliy:
                mTransaction.replace(R.id.frame_cont, new DaliyEventsFragment(), Constants.FragmentTagDaliy);
                break;
            case R.id.nav_share:
                mShareAction.open();
                break;
            case R.id.nav_about:
                mTransaction.replace(R.id.frame_cont, new AboutUs(), Constants.FragmentTagAbout);
                break;
            default: {
                mTransaction.replace(R.id.frame_cont, new GankFragment(), Constants.FragmentTagNews);
                break;
            }
        }
        mTransaction.commitAllowingStateLoss();
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
