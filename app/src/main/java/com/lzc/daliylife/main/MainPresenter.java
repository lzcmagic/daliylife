package com.lzc.daliylife.main;

import android.support.v4.app.FragmentActivity;

import com.lzc.daliylife.entity.LocationEntity;
import com.lzc.daliylife.entity.mob.WeatherEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.http.HttpMethods;
import com.lzc.daliylife.utils.AMapUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lzc on 2017/9/7.
 *
 */

public class MainPresenter implements MainContract.MPresenter {

    private MainActivity mActivity;
    private MainContract.MView mView;

    MainPresenter(MainContract.MView mView) {
        this.mView = mView;
    }

    @Override
    public void attachView(FragmentActivity fmActivity) {
        this.mActivity= (MainActivity) fmActivity;
    }

    @Override
    public void detachView() {
        if (disposable!=null){
            disposable.dispose();
        }
        this.mActivity=null;
    }
    private Disposable disposable;

    @Override
    public void refreshWeather() {
        mView.showDialog();
        AMapUtils.getInstance().startLocation(new AMapUtils.SendLocation() {
            @Override
            public void sendLocation(final LocationEntity entity) {
                HttpMethods.getInstance(Constants.WEATHERAPI).getWeekWeather(new Observer<WeatherEntity>() {


                    @Override
                    public void onError(Throwable e) {
                        mView.hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideDialog();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(WeatherEntity weatherEntity) {
                        WeatherEntity.Result result = weatherEntity.getresult().get(0);
                        String weatherInfo = result.getWeather();
                        String temperatureInfo = result.getTemperature();
                        String weatherTempInfo = result.getfuture().get(0).getTemperature();
                        mView.showWeather(weatherInfo,temperatureInfo,weatherTempInfo,entity.getDistrict());
                    }
                }, Constants.WEATHERKEY, entity.getCity().replace("市", ""), entity.getProvince());
            }
        });
    }

    @Override
    public void initWeather() {
        String currentWeather = mActivity.getIntent().getStringExtra("weather");
        String currentWeatherText = mActivity.getIntent().getStringExtra("weatherText");
        String currentTemperature = mActivity.getIntent().getStringExtra("temperature");
        String district = mActivity.getIntent().getStringExtra("district");
        mView.showWeather(currentWeather, currentWeatherText, currentTemperature,district);
    }
}
