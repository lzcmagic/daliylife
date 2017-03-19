package com.example.lzc.daliylife.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.entity.MovieEntity;
import com.example.lzc.daliylife.entity.WeatherEntity;
import com.example.lzc.daliylife.fragments.DaliyEventsFragment;
import com.example.lzc.daliylife.fragments.GankFragment;
import com.example.lzc.daliylife.fragments.OtherFragment;
import com.example.lzc.daliylife.fragments.WeChartFragment;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.utils.HttpMethods;
import com.example.lzc.daliylife.utils.ProgressSubscriber;
import com.example.lzc.daliylife.utils.SubscriberOnNextListener;
import com.example.lzc.daliylife.utils.WeatherToIcon;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.frame_cont)
    FrameLayout container;
    private String CurrentWeather;
    private String CurrentWeatherText;
    private String CurrentTemperature;
    private ProgressDialog mPDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initProgressDialog();
        CurrentWeather = getIntent().getStringExtra("weather");
        CurrentWeatherText = getIntent().getStringExtra("weatherText");
        CurrentTemperature = getIntent().getStringExtra("temperature");

        navView.setNavigationItemSelectedListener(this);
        ColorStateList stateList = getResources().getColorStateList(R.color.navigation_menu_item_color);
        navView.setItemIconTintList(stateList);
        navView.setItemTextColor(stateList);
        navView.getMenu().getItem(0).setChecked(true);

        initNavHeadView();
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.frame_cont, new GankFragment());
            transaction.commit();
        }
    }

    /**
     * 初始化抽屉
     * @param mToolbar
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

    private void initNavHeadView() {
        View headerView = navView.getHeaderView(0);
        final ImageView image = (ImageView) headerView.findViewById(R.id.imageView);
        final TextView weather = (TextView) headerView.findViewById(R.id.weather);
        final TextView weatherText = (TextView) headerView.findViewById(R.id.weather_text);
        final TextView weatherTemp = (TextView) headerView.findViewById(R.id.weather_temp);
        if (!TextUtils.isEmpty(CurrentWeather)) {
            weather.setText(CurrentWeather);
            image.setImageResource(WeatherToIcon.Weather2Icon(CurrentWeather));
        } else {
            //获取天气信息失败
            weather.setText("天气信息获取失败,点击图标重试");
        }
        if (!TextUtils.isEmpty(CurrentWeatherText)) {
            weatherText.setText(CurrentWeatherText);
        } else {
            //获取天气信息失败
            weatherText.setText("");
        }
        if (!TextUtils.isEmpty(CurrentTemperature)) {
            weatherTemp.setText(CurrentTemperature);
        } else {
            //获取天气信息失败
            weatherTemp.setText("");
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPDialog.show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HttpMethods.getInstance(Constants.WEATHERAPI).getWeekWeather(new Subscriber<WeatherEntity>() {

                            @Override
                            public void onCompleted() {
                                mPDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mPDialog.dismiss();
                            }

                            @Override
                            public void onNext(WeatherEntity weatherEntity) {
                                WeatherEntity.Result result = weatherEntity.getresult().get(0);
                                String weatherInfo = result.getWeather();
                                String temperatureInfo = result.getTemperature();
                                String weatherTempInfo = result.getfuture().get(0).getTemperature();
                                if (!TextUtils.isEmpty(weatherInfo)) {
                                    weather.setText(weatherInfo);
                                    image.setImageResource(WeatherToIcon.Weather2Icon(weatherInfo));
                                } else {
                                    //获取天气信息失败
                                    weather.setText("天气信息获取失败,稍后再试");
                                }
                                if (!TextUtils.isEmpty(temperatureInfo)) {
                                    weatherText.setText(temperatureInfo);
                                } else {
                                    //获取天气信息失败
                                    weatherText.setText("");
                                }
                                if (!TextUtils.isEmpty(weatherTempInfo)) {
                                    weatherTemp.setText(weatherTempInfo);
                                } else {
                                    //获取天气信息失败
                                    weatherTemp.setText("");
                                }
                            }
                        }, Constants.WEATHERKEY, "无锡", "江苏");
                    }
                },1000);

            }
        });
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction mTransaction =
                getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        if (id == R.id.news) {
            mTransaction.replace(R.id.frame_cont, new GankFragment(), Constants.FragmentTagNews);
        } else if (id == R.id.wechart) {
            mTransaction.replace(R.id.frame_cont, new WeChartFragment(), Constants.FragmentTagWeChart);
        } else if (id == R.id.car) {
            mTransaction.replace(R.id.frame_cont, new OtherFragment(), Constants.FragmentTagLottery);
        } else if (id == R.id.daliy) {
            mTransaction.replace(R.id.frame_cont, new DaliyEventsFragment(), Constants.FragmentTagDaliy);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        } else {

        }
        mTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getMovie() {
        HttpMethods.getInstance(Constants.DOUBANAPI).getTopMovie(new ProgressSubscriber<MovieEntity>(new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                if (o instanceof MovieEntity) {
                    Log.d(Constants.NORMALTAG, ((MovieEntity) o).toString());
                    // resultTV.setText(((MovieEntity) o).toString());
                }
            }
        }), 0, 10);
    }

}
