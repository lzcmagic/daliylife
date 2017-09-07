package com.lzc.daliylife.httplistener;

import com.lzc.daliylife.entity.LotteryEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lzc on 2017/3/18.
 */

public interface LotteryServiceObserv {
    @GET("query")
    Observable<LotteryEntity> getLotteryInfo(@Query("key") String key,@Query("name") String name);
}
