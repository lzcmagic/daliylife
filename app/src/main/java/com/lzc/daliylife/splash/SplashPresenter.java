package com.lzc.daliylife.splash;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.lzc.daliylife.entity.LocationEntity;
import com.lzc.daliylife.entity.WeatherEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.normalUtil.L;
import com.lzc.daliylife.utils.AMapUtils;
import com.lzc.daliylife.utils.HttpMethods;

import rx.Subscriber;

/**
 * Created by lzc on 2017/9/7.
 */

public class SplashPresenter implements SplashContract.SPresenter {

    private SplashActivity mActivity;
    private SplashContract.SView mView;
    private WeatherEntity mWeather;

    public SplashPresenter(SplashContract.SView mView) {
        this.mView = mView;
    }

    @Override
    public void attachView(FragmentActivity fmActivity) {
        this.mActivity= (SplashActivity) fmActivity;
    }

    @Override
    public void detachView() {
        this.mActivity=null;
    }

    @Override
    public void initIntent() {
        mView.startIntent(mWeather);
    }


    @Override
    public void startLocation() {
        AMapUtils.getInstance().startLocation(new AMapUtils.SendLocation() {
            @Override
            public void sendLocation(LocationEntity entity) {
                if (entity != null) {
                    L.d("startLocation: " + entity.toString());
                    loadData(entity);
                }
            }
        });
        mView.startAnimation();
    }



    private void loadData(LocationEntity LocationEntity) {
        HttpMethods.getInstance(Constants.WEATHERAPI).getWeekWeather(new Subscriber<WeatherEntity>() {
            @Override
            public void onCompleted() {
                Log.d("lzcc", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                L.d("onError");
                mWeather = null;
            }

            @Override
            public void onNext(WeatherEntity weatherEntity) {
                L.d("onNext");
                mWeather = weatherEntity;
            }
        }, Constants.WEATHERKEY, LocationEntity.getCity().replace("市", ""), LocationEntity.getProvince());
    }
}
