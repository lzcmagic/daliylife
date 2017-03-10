package com.example.lzc.daliylife.httplistener;

import com.example.lzc.daliylife.entity.LaoHuangLiEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lzc on 2017/3/10.
 */

public interface LaoHuangLiServiceObserv {
    @GET("day")
    Observable<LaoHuangLiEntity> getDayLHL(
            @Query("key") String key,
            @Query("date") String date);
}
