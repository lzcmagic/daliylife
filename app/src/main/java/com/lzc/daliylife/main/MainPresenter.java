package com.lzc.daliylife.main;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.lzc.daliylife.entity.LocationEntity;
import com.lzc.daliylife.entity.WeatherEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.utils.AMapUtils;
import com.lzc.daliylife.utils.HttpMethods;
import com.lzc.daliylife.utils.WeatherToIcon;

import rx.Subscriber;

/**
 * Created by lzc on 2017/9/7.
 */

public class MainPresenter implements MainContract.MPresenter {

    private MainActivity mActivity;
    private MainContract.MView mView;
    private String CurrentWeather;
    private String CurrentWeatherText;
    private String CurrentTemperature;

    public MainPresenter(MainContract.MView mView) {
        this.mView = mView;
    }

    @Override
    public void attachView(FragmentActivity fmActivity) {
        this.mActivity= (MainActivity) fmActivity;
    }

    @Override
    public void detachView() {
        this.mActivity=null;
    }

    @Override
    public void refreshWeather() {
        mView.showDialog();
        AMapUtils.getInstance().startLocation(new AMapUtils.SendLocation() {
            @Override
            public void sendLocation(LocationEntity entity) {
                HttpMethods.getInstance(Constants.WEATHERAPI).getWeekWeather(new Subscriber<WeatherEntity>() {

                    @Override
                    public void onCompleted() {
                        mView.hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideDialog();
                    }

                    @Override
                    public void onNext(WeatherEntity weatherEntity) {
                        WeatherEntity.Result result = weatherEntity.getresult().get(0);
                        String weatherInfo = result.getWeather();
                        String temperatureInfo = result.getTemperature();
                        String weatherTempInfo = result.getfuture().get(0).getTemperature();
                        mView.showWeather(weatherInfo,temperatureInfo,weatherTempInfo);
                    }
                }, Constants.WEATHERKEY, entity.getCity().replace("市", ""), entity.getProvince());
            }
        });
    }

    @Override
    public void initWeather() {
        CurrentWeather = mActivity.getIntent().getStringExtra("weather");
        CurrentWeatherText =mActivity. getIntent().getStringExtra("weatherText");
        CurrentTemperature = mActivity.getIntent().getStringExtra("temperature");
        mView.showWeather(CurrentWeather,CurrentWeatherText,CurrentTemperature);
    }
}
