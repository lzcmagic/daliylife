package com.example.lzc.daliylife.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.entity.WeatherEntity;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.normalUtil.T;
import com.example.lzc.daliylife.utils.HttpMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by lzc on 2016/11/14.
 */

public class SplashActivity extends AppCompatActivity {
    ScaleAnimation scaleAnimation;
    private WeatherEntity CurrentWeather;
    @BindView(R.id.tv_wel)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initAnimation();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        scaleAnimation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (CurrentWeather != null) {
                    WeatherEntity.Result result = CurrentWeather.getresult().get(0);
                    WeatherEntity.Result.Future future = CurrentWeather.getresult().get(0).getfuture().get(0);
                    MainActivity.actionStart(getApplicationContext(), result.getWeather(), result.getTemperature(),future.getTemperature());
                }else{
                    T.showLong("天气信息获取失败");
                    MainActivity.actionStart(getApplicationContext(), null, null,null);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpMethods.getInstance(Constants.WEATHERAPI).getWeekWeather(new Subscriber<WeatherEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                CurrentWeather = null;
            }

            @Override
            public void onNext(WeatherEntity weatherEntity) {
                CurrentWeather = weatherEntity;
            }
        }, Constants.WEATHERKEY, "无锡", "江苏");
        textView.startAnimation(scaleAnimation);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textView.clearAnimation();
    }
}
