package com.lzc.daliylife.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.about.AboutUs;
import com.lzc.daliylife.article.ArticleFragment;
import com.lzc.daliylife.base.BaseActivity;
import com.lzc.daliylife.calender.DaliyEventsFragment;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.gank.GankFragment;
import com.lzc.daliylife.lottery.LotteryFragment;
import com.lzc.daliylife.utils.ActivityUtils;
import com.lzc.daliylife.utils.WeatherToIcon;

import butterknife.BindView;

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ProgressDialog mPDialog;


    private MainPresenter mainPresenter;

    @Override
    protected void onStart() {
        super.onStart();
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.frame_cont);
        if (fragmentById==null){
            GankFragment gankFragment = GankFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    gankFragment, R.id.frame_cont);
        }

        //初始化chrome
        CustomTabsClient.connectAndInitialize(this, "com.android.chrome");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void initUI() {
        /*增加自定义按钮的分享面板*/
        mainPresenter.initWeather();
        initProgressDialog();
        navView.setNavigationItemSelectedListener(this);
        ColorStateList stateList = getResources().getColorStateList(R.color.navigation_menu_item_color);
        navView.setItemIconTintList(stateList);
        navView.setItemTextColor(stateList);
        navView.getMenu().getItem(0).setChecked(true);

        initToolBar();


    }

    private void initToolBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getResources().getString(R.string.toolbar_news));
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }


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
    public void showWeather(String weather, String weatherText, String temperature) {
        View headerView = navView.getHeaderView(0);
        ImageView weatherIconImg = (ImageView) headerView.findViewById(R.id.imageView);
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
        weatherIconImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.refreshWeather();
            }
        });

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


    private void initProgressDialog() {
        mPDialog = new ProgressDialog(this);
        mPDialog.setMessage("正在加载中...");
    }

    /**
     * intent
     *
     * @param context 上下文
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction mTransaction =
                getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.gank:
                toolbar.setTitle(getResources().getString(R.string.toolbar_news));
                mTransaction.replace(R.id.frame_cont, new GankFragment(), Constants.FragmentTagNews);
                break;
            case R.id.wechart:
                toolbar.setTitle(getResources().getString(R.string.toolbar_wechart));
                mTransaction.replace(R.id.frame_cont, new ArticleFragment(), Constants.FragmentTagWeChart);
                break;
            case R.id.lottery:
                toolbar.setTitle(getResources().getString(R.string.toolbar_lottery));
                mTransaction.replace(R.id.frame_cont, new LotteryFragment(), Constants.FragmentTagLottery);
                break;
            case R.id.daliy:
                toolbar.setTitle(getResources().getString(R.string.toolbar_daliy));
                mTransaction.replace(R.id.frame_cont, new DaliyEventsFragment(), Constants.FragmentTagDaliy);
                break;
            case R.id.nav_about:
                toolbar.setTitle(getResources().getString(R.string.toolbar_about));
                mTransaction.replace(R.id.frame_cont, new AboutUs(), Constants.FragmentTagAbout);
                break;
            default: {
                toolbar.setTitle(getResources().getString(R.string.toolbar_news));
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
