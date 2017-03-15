package com.example.lzc.daliylife.httplistener;

import com.example.lzc.daliylife.entity.AndroidEntity;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lzc on 2017/3/15.
 */

public interface AndroidServiceObserv {
    @GET("data/{type}/{number}/{page}")
    Observable<AndroidEntity> getAndroidInfo(@Path("type") String type,@Path("number") int number,@Path("page") int page);
}
