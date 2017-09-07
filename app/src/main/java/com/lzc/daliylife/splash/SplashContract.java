package com.lzc.daliylife.splash;

import com.lzc.daliylife.base.BasePresenter;
import com.lzc.daliylife.base.BaseView;
import com.lzc.daliylife.entity.WeatherEntity;

/**
 * Created by lzc on 2017/9/7.
 */

public interface SplashContract {
    interface SPresenter extends BasePresenter {


        void initIntent();

        void startLocation();
    }

    interface SView extends BaseView {
        void startIntent(WeatherEntity weatherEntity);
        void startAnimation();


    }
}
