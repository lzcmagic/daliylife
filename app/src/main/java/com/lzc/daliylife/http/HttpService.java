package com.lzc.daliylife.http;

import com.lzc.daliylife.entity.mob.LaoHuangLiEntity;
import com.lzc.daliylife.entity.mob.LotteryEntity;
import com.lzc.daliylife.entity.MovieEntity;
import com.lzc.daliylife.entity.yiyuan.BSQJEntity;
import com.lzc.daliylife.entity.yiyuan.SummaryLotteryEntity;
import com.lzc.daliylife.entity.mob.WeatherEntity;
import com.lzc.daliylife.entity.juhe.WechatEntity;
import com.lzc.daliylife.entity.gankentity.AndroidEntity;
import com.lzc.daliylife.entity.gankentity.FuLiEntity;
import com.lzc.daliylife.entity.gankentity.IOSEntity;
import com.lzc.daliylife.entity.gankentity.QianDuanEntity;
import com.lzc.daliylife.entity.gankentity.TuoZhanEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by lzc on 2017/9/8.
 */

public interface HttpService {
    @GET("data/{type}/{number}/{page}")
    Observable<AndroidEntity> getAndroidInfo(@Path("type") String type, @Path("number") int number, @Path("page") int page);

    @GET("data/{type}/{number}/{page}")
    Observable<FuLiEntity> getFuLiInfo(@Path("type") String type, @Path("number") int number, @Path("page") int page);

    @GET("data/{type}/{number}/{page}")
    Observable<IOSEntity> getIOSInfo(@Path("type") String type, @Path("number") int number, @Path("page") int page);

    @GET("day")
    Observable<LaoHuangLiEntity> getDayLHL(
            @Query("key") String key,
            @Query("date") String date);

    //test
    @GET("top250")
    Call<MovieEntity> getTopMovie(@Query("start") int start , @Query("count") int count);

    //test
    @GET("top250")
    Observable<MovieEntity> getTopMovieO(@Query("start") int start, @Query("count") int count);

    @GET("data/{type}/{number}/{page}")
    Observable<QianDuanEntity> getQianDuanInfo(@Path("type") String type, @Path("number") int number, @Path("page") int page);

    @GET("data/{type}/{number}/{page}")
    Observable<TuoZhanEntity> getTuoZhanInfo(@Path("type") String type, @Path("number") int number, @Path("page") int page);

    @GET("query")
    Observable<WeatherEntity> getWeekWeather(
            @Query("key") String key,
            @Query("city") String city,
            @Query("province") String province);

    @GET("query")
    Observable<WechatEntity> getWechatInfo(@Query("pno") int pne, @Query("ps") int ps, @Query("dtype") String dtype, @Query("key") String key);

    @GET("query")
    Observable<LotteryEntity> getLotteryInfo(@Query("key") String key, @Query("name") String name);


    @GET("/{type}")
    Observable<SummaryLotteryEntity> getYiYuanLotteryInfo(@Path("type") String type, @QueryMap Map<String,String> map);


    @GET("/{type}")
    Observable<BSQJEntity> getBSQJInfo(@Path("type") String type, @QueryMap Map<String,String> map);

}
