package com.lzc.daliylife.main;

import com.lzc.daliylife.base.BasePresenter;
import com.lzc.daliylife.base.BaseView;

/**
 * Created by lzc on 2017/9/7.
 */

public interface MainContract {
    interface MPresenter extends BasePresenter {

        void refreshWeather();

        void initWeather();

    }

    interface MView extends BaseView {

        void showWeather(String weather,String weatherText,String temperature,String district);


        void showDialog();

        void hideDialog();
    }
}
