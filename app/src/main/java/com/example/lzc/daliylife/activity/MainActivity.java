package com.example.lzc.daliylife.activity;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.entity.LocationEntity;
import com.example.lzc.daliylife.entity.WeatherEntity;
import com.example.lzc.daliylife.fragments.AboutUs;
import com.example.lzc.daliylife.fragments.DaliyEventsFragment;
import com.example.lzc.daliylife.fragments.GankFragment;
import com.example.lzc.daliylife.fragments.OtherFragment;
import com.example.lzc.daliylife.fragments.WeChartFragment;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.normalUtil.T;
import com.example.lzc.daliylife.utils.BaiduMapUtil;
import com.example.lzc.daliylife.utils.HttpMethods;
import com.example.lzc.daliylife.utils.WeatherToIcon;
import com.tapadoo.alerter.Alerter;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

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
    DrawerLayout drawerLayout;
    @BindView(R.id.frame_cont)
    FrameLayout container;
    private String CurrentWeather;
    private String CurrentWeatherText;
    private String CurrentTemperature;
    private ProgressDialog mPDialog;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.frame_cont, new GankFragment());
            transaction.commit();
        }
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


        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(MainActivity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.WEIXIN_FAVORITE,
                SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE,
                SHARE_MEDIA.MORE)
                .addButton("umeng_sharebutton_copy",
                        "umeng_sharebutton_copy",
                        "umeng_socialize_copy",
                        "umeng_socialize_copy")
                .addButton("umeng_sharebutton_copyurl",
                        "umeng_sharebutton_copyurl",
                        "umeng_socialize_copyurl",
                        "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copy")) {
                            Toast.makeText(MainActivity.this, "复制文本按钮", Toast.LENGTH_LONG).show();
                        } else if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            Toast.makeText(MainActivity.this, "复制链接按钮", Toast.LENGTH_LONG).show();
                        } else {
                            if (share_media == SHARE_MEDIA.QQ ||
                                    share_media == SHARE_MEDIA.QZONE) {
                                UMImage image = new UMImage(MainActivity.this, R.mipmap.lottery_dlt);
                                new ShareAction(MainActivity.this).withMedia(image)
                                        .setPlatform(share_media)
                                        .setCallback(mShareListener)
                                        .share();
                            } else {
                                new ShareAction(MainActivity.this).withText("体验一下休闲时光App吧")
                                        .setPlatform(share_media)
                                        .setCallback(mShareListener)
                                        .share();
                            }

                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<MainActivity> mActivity;

        private CustomShareListener(MainActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                T.showShort(platform + " 收藏成功");
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Alerter.create(mActivity.get())
                            .setTitle("提示")
                            .setText("分享成功")
                            .setBackgroundColor(R.color.colorAccent)
                            .setIcon(R.mipmap.xiaolian)
                            .setDuration(500)
                            .show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Alerter.create(mActivity.get())
                        .setTitle("提示")
                        .setText("分享失败")
                        .setBackgroundColor(android.R.color.holo_red_dark)
                        .setIcon(R.mipmap.kulian)
                        .setDuration(500)
                        .show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            T.showShort(platform + " 取消分享");
        }
    }

    /**
     * 初始化抽屉
     *
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
                //请求位置
                BaiduMapUtil.getInstance().startLocation(new BaiduMapUtil.SendLocation() {
                    @Override
                    public void sendLocation(final LocationEntity entity) {
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
                        }, Constants.WEATHERKEY, entity.getCity().replace("市", ""), entity.getProvince());
                    }
                });
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
            mShareAction.open();
        } else if (id == R.id.nav_about) {
            mTransaction.replace(R.id.frame_cont, new AboutUs(), Constants.FragmentTagAbout);
        } else {

        }
        mTransaction.commit();
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
