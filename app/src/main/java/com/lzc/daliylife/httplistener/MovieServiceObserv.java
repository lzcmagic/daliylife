package com.lzc.daliylife.httplistener;


import com.lzc.daliylife.entity.MovieEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lzc on 2016/11/9.
 */
public interface MovieServiceObserv {
    @GET("top250")
    Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
}
