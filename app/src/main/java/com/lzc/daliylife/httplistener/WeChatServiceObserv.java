package com.lzc.daliylife.httplistener;

import com.lzc.daliylife.entity.WechatEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lzc on 2017/3/20.
 */

public interface WeChatServiceObserv {
    @GET("query")
    Observable<WechatEntity> getWechatInfo(@Query("pno") int pne, @Query("ps") int ps, @Query("dtype") String dtype,@Query("key") String key);
}
