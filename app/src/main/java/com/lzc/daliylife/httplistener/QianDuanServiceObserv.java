package com.lzc.daliylife.httplistener;

import com.lzc.daliylife.entity.gankentity.QianDuanEntity;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lzc on 2017/3/15.
 */

public interface QianDuanServiceObserv {
    @GET("data/{type}/{number}/{page}")
    Observable<QianDuanEntity> getQianDuanInfo(@Path("type") String type, @Path("number") int number, @Path("page") int page);
}
