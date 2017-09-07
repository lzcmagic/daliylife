package com.lzc.daliylife.httplistener;

import com.lzc.daliylife.entity.gankentity.FuLiEntity;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lzc on 2017/3/14.
 */

public interface FuLiServiceObserv {
    @GET("data/{type}/{number}/{page}")
    Observable<FuLiEntity> getFuLiInfo(@Path("type") String type, @Path("number") int number, @Path("page") int page);
}
