package com.lzc.daliylife.httplistener;

import com.lzc.daliylife.entity.MovieEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lzc on 2016/11/9.
 */
public interface MovieService {
    @GET("top250")
    Call<MovieEntity> getTopMovie(@Query("start") int start , @Query("count") int count);
}
