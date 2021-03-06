package com.lzc.daliylife.splash;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.lzc.daliylife.entity.LocationEntity;
import com.lzc.daliylife.entity.mob.WeatherEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.http.HttpMethods;
import com.lzc.daliylife.normalUtil.L;
import com.lzc.daliylife.utils.AMapUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lzc on 2017/9/7.
 *
 */

class SplashPresenter implements SplashContract.SPresenter {

    private SplashContract.SView mView;
    private WeatherEntity mWeather;
    private Disposable disposable;
    private LocationEntity locationEntity;

    SplashPresenter(SplashContract.SView mView) {
        this.mView = mView;
    }

    @Override
    public void attachView(FragmentActivity fmActivity) {
    }

    @Override
    public void detachView() {
        if (disposable != null)
            disposable.dispose();

    }

    @Override
    public void initIntent() {
        AMapUtils.getInstance().stopLocation();
        mView.startIntent(mWeather,locationEntity);
    }


    @Override
    public void startLocation() {
        AMapUtils.getInstance().startLocation(new AMapUtils.SendLocation() {
            @Override
            public void sendLocation(LocationEntity entity) {
                if (entity != null) {
                    locationEntity=entity;
                    L.d("startLocation: " + entity.toString());
                    loadData(entity);
                }
            }
        });
        mView.startAnimation();
    }



    private void loadData(LocationEntity LocationEntity) {
        HttpMethods.getInstance(Constants.WEATHERAPI).getWeekWeather(new Observer<WeatherEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(WeatherEntity value) {
                L.d("onNext");
                mWeather = value;
            }

            @Override
            public void onError(Throwable e) {
                L.d("onError");
                mWeather = null;
            }

            @Override
            public void onComplete() {
                Log.d("lzcc", "onCompleted");
            }
        }, Constants.WEATHERKEY, LocationEntity.getCity().replace("市", ""), LocationEntity.getProvince());
    }
}
