package com.lzc.daliylife.splash;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.base.BaseActivity;
import com.lzc.daliylife.entity.LocationEntity;
import com.lzc.daliylife.entity.mob.WeatherEntity;
import com.lzc.daliylife.main.MainActivity;
import com.lzc.daliylife.normalUtil.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lzc on 2016/11/14.
 *
 */

public class SplashActivity extends BaseActivity implements SplashContract.SView {
    private AnimatorSet animatorSet;
    private static int GRANT_PERMISSION = 1;
    @BindView(R.id.iv_splash)
    ImageView mImage;
    private List<String> permissionList = new ArrayList<>();

    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUI() {
        permissionList.add(Manifest.permission.READ_PHONE_STATE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        initAnimation();
    }

    @Override
    public void initPresenter() {
        splashPresenter = new SplashPresenter(this);
        splashPresenter.attachView(this);
    }

    @Override
    public int getResId() {
        return R.layout.activity_welcome;
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        animatorSet = new AnimatorSet();

        ObjectAnimator scaleX=ObjectAnimator.ofFloat(mImage,"scaleX",1f,0f);
        ObjectAnimator scaleY=ObjectAnimator.ofFloat(mImage,"scaleY",1f,0f);
        animatorSet.playTogether(scaleX,scaleY);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(1500);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                splashPresenter.initIntent();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        int permissionReadPhone = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionReadPhone != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                T.showShort("是否同意程序读取手机当前状态");
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, GRANT_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, GRANT_PERMISSION);
            }
        } else {
            splashPresenter.startLocation();
        }
    }


    @Override
    public void startIntent(WeatherEntity weatherEntity,LocationEntity locationEntity) {
        if (weatherEntity != null) {
            WeatherEntity.Result result = weatherEntity.getresult().get(0);
            WeatherEntity.Result.Future future = weatherEntity.getresult().get(0).getfuture().get(0);
            MainActivity.actionStart(this,
                    result.getWeather(),
                    result.getTemperature(),
                    future.getTemperature(),
                    locationEntity.getDistrict());
        } else {
            T.showLong("天气信息获取失败");
            MainActivity.actionStart(this, null, null, null,null);
        }
        finish();
    }

    @Override
    public void startAnimation() {
        animatorSet.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GRANT_PERMISSION) {
            for (int i = 0; i < permissionList.size(); i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    T.showShort("权限" + (i + 1) + "申请未通过，程序运行时功能可能不会正常运行");
                    return;
                }
            }
            //同意了读取手机状态权限
            splashPresenter.startLocation();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImage != null)
            mImage.clearAnimation();
        if (splashPresenter != null)
            splashPresenter.detachView();
    }

}
