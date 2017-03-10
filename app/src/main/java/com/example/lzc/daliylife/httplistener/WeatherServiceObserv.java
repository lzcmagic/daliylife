package com.example.lzc.daliylife.httplistener;

import com.example.lzc.daliylife.entity.WeatherEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lzc on 2017/3/10.
 */

public interface WeatherServiceObserv {
    @GET("query")
    Observable<WeatherEntity> getWeekWeather(
            @Query("key") String key,
            @Query("city") String city,
            @Query("province") String province);
}
