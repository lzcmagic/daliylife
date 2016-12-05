package com.example.lzc.retrofitandrxjavatest.httplistener;


import com.example.lzc.retrofitandrxjavatest.entity.MovieEntity;

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
