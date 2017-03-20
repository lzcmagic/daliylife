package com.example.lzc.daliylife.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.entity.LocationEntity;
import com.example.lzc.daliylife.entity.WeatherEntity;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.normalUtil.T;
import com.example.lzc.daliylife.utils.BaiduMapUtil;
import com.example.lzc.daliylife.utils.HttpMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by lzc on 2016/11/14.
 */

public class SplashActivity extends AppCompatActivity {
    ScaleAnimation scaleAnimation;
    private WeatherEntity CurrentWeather;
    private static int READ_PHONE_STATE = 1;
    @BindView(R.id.tv_wel)
    TextView textView;
    private List<String> permissionList = new ArrayList<>();
    private LocationEntity LocationEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        permissionList.add(Manifest.permission.READ_PHONE_STATE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        //注册监听函数
        ButterKnife.bind(this);
        initAnimation();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        scaleAnimation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                IntentMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_PHONE_STATE) {
            for (int i = 0; i < permissionList.size(); i++) {
                Log.d(Constants.NORMALTAG, permissions[i] + " " + grantResults[i]);
                if (permissions[i].equals(permissionList.get(i)) &&
                        grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //同意了读取手机状态权限

                } else {
                    T.showShort("权限" + (i + 1) + "申请未通过，程序运行时功能可能不会正常运行");
                }
                BaiduMapUtil.getInstance().startLocation(new BaiduMapUtil.SendLocation() {
                    @Override
                    public void sendLocation(LocationEntity entity) {
                        LocationEntity = entity;
                        loadData();
                    }
                });
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void IntentMainActivity() {
        if (CurrentWeather != null) {
            Log.d(Constants.NORMALTAG, CurrentWeather.toString());
            WeatherEntity.Result result = CurrentWeather.getresult().get(0);
            WeatherEntity.Result.Future future = CurrentWeather.getresult().get(0).getfuture().get(0);
            MainActivity.actionStart(getApplicationContext(), result.getWeather(), result.getTemperature(), future.getTemperature());
        } else {
            T.showLong("天气信息获取失败");
            MainActivity.actionStart(getApplicationContext(), null, null, null);
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int permissionReadPhone = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (permissionReadPhone != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_PHONE_STATE)) {

                T.showShort("是否同意程序读取手机当前状态");
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, READ_PHONE_STATE);
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, READ_PHONE_STATE);
            }

        } else {
            //权限通过 开始定位服务
            BaiduMapUtil.getInstance().startLocation(new BaiduMapUtil.SendLocation() {
                @Override
                public void sendLocation(LocationEntity entity) {
                    LocationEntity = entity;
                    loadData();
                }
            });
        }
    }

    private void loadData() {
        HttpMethods.getInstance(Constants.WEATHERAPI).getWeekWeather(new Subscriber<WeatherEntity>() {
            @Override
            public void onCompleted() {
                textView.startAnimation(scaleAnimation);
            }

            @Override
            public void onError(Throwable e) {
                CurrentWeather = null;
                textView.startAnimation(scaleAnimation);
            }

            @Override
            public void onNext(WeatherEntity weatherEntity) {
                CurrentWeather = weatherEntity;
            }
        }, Constants.WEATHERKEY, LocationEntity.getCity().replace("市", ""), LocationEntity.getProvince());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textView.clearAnimation();
        BaiduMapUtil.getInstance().stopLocation();
    }
}
