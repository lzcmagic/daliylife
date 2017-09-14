package com.lzc.daliylife.utils;

import com.lzc.daliylife.R;

/**
 * Created by lzc on 2017/3/18.
 *
 */

public class WeatherToIcon {

    public static int Weather2Icon(String weather){

        if (weather.startsWith("晴")){
            return R.mipmap.weather_sun;
        }else if (weather.startsWith("多云")){
            return R.mipmap.weather_cloud;
        }
        else if (weather.contains("雨")){
            return R.mipmap.weather_xiaoyu;
        }
        else if (weather.startsWith("阴")){
            return R.mipmap.weather_yin;
        }
        else if (weather.contains("雷")){
            return R.mipmap.weather_lei;
        }
        else if (weather.contains("雪")){
            return R.mipmap.weather_xue;
        }
        else{
            return R.mipmap.weather_sun;
        }
    }
}
