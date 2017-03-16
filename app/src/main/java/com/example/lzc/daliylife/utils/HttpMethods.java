package com.example.lzc.daliylife.utils;

import android.util.Log;

import com.example.lzc.daliylife.entity.gankentity.AndroidEntity;
import com.example.lzc.daliylife.entity.gankentity.FuLiEntity;
import com.example.lzc.daliylife.entity.gankentity.IOSEntity;
import com.example.lzc.daliylife.entity.LaoHuangLiEntity;
import com.example.lzc.daliylife.entity.MovieEntity;
import com.example.lzc.daliylife.entity.gankentity.QianDuanEntity;
import com.example.lzc.daliylife.entity.gankentity.TuoZhanEntity;
import com.example.lzc.daliylife.entity.WeatherEntity;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.httplistener.AndroidServiceObserv;
import com.example.lzc.daliylife.httplistener.FuLiServiceObserv;
import com.example.lzc.daliylife.httplistener.IosServiceObserv;
import com.example.lzc.daliylife.httplistener.LaoHuangLiServiceObserv;
import com.example.lzc.daliylife.httplistener.MovieServiceObserv;
import com.example.lzc.daliylife.httplistener.QianDuanServiceObserv;
import com.example.lzc.daliylife.httplistener.TuoZhanServiceObserv;
import com.example.lzc.daliylife.httplistener.WeatherServiceObserv;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lzc on 2016/11/14.
 */
public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private MovieServiceObserv movieService;
    private WeatherServiceObserv weatherServiceObserv;
    private LaoHuangLiServiceObserv laoHuangLiServiceObserv;
    private FuLiServiceObserv fuLiServiceObserv;
    private AndroidServiceObserv androidServiceObserv;
    private IosServiceObserv iosServiceObserv;
    private QianDuanServiceObserv qianDuanServiceObserv;
    private TuoZhanServiceObserv tuoZhanServiceObserv;
    private static HttpMethods SingleInstance;

    //构造方法私有
    private HttpMethods(String url) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i("RetrofitLog", "retrofitBack = " + message);
            }
        });
        //1.NONE 不打印
        //2.BASIC 请求/响应行
        //3.HEADERS 请求/响应行+头
        //4.BODY 请求/响应行 + 头 + 体
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        Log.d(Constants.NORMALTAG, url);

    }


    //获取单例 饿汉模式
    public static synchronized HttpMethods getInstance(String url) {
        SingleInstance = new HttpMethods(url);
        return SingleInstance;
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param start      起始位置
     * @param count      获取长度
     */
    public void getTopMovie(Subscriber<MovieEntity> subscriber, int start, int count) {
        movieService = retrofit.create(MovieServiceObserv.class);
        movieService.getTopMovie(start, count)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * get near week weather
     *
     * @param subscriber
     * @param key
     * @param city
     * @param province
     */
    public void getWeekWeather(Subscriber<WeatherEntity> subscriber, String key, String city, String province) {
        weatherServiceObserv = retrofit.create(WeatherServiceObserv.class);
        weatherServiceObserv.getWeekWeather(key, city, province)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取老黄历信息
     *
     * @param subscriber
     * @param key
     * @param date
     */
    public void getDayLHL(Subscriber<LaoHuangLiEntity> subscriber, String key, String date) {
        laoHuangLiServiceObserv = retrofit.create(LaoHuangLiServiceObserv.class);
        laoHuangLiServiceObserv.getDayLHL(key, date)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取福利数据
     *
     * @param subscriber
     * @param type
     * @param number
     * @param page
     */
    public void getFuLiInfo(Subscriber<FuLiEntity> subscriber, String type, int number, int page) {
        fuLiServiceObserv = retrofit.create(FuLiServiceObserv.class);
        fuLiServiceObserv.getFuLiInfo(type, number, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 获取android信息
     *
     * @param subscriber
     * @param type
     * @param number
     * @param page
     */
    public void getAndroidInfo(Subscriber<AndroidEntity> subscriber, String type, int number, int page) {
        androidServiceObserv = retrofit.create(AndroidServiceObserv.class);
        androidServiceObserv.getAndroidInfo(type, number, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取IOS信息
     *
     * @param subscribe
     * @param type
     * @param number
     * @param page
     */
    public void getIOSInfo(Subscriber<IOSEntity> subscribe, String type, int number, int page) {
        iosServiceObserv = retrofit.create(IosServiceObserv.class);
        iosServiceObserv.getIOSInfo(type, number, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscribe);
    }

    /**
     * 获取前端信息
     * @param subscriber
     * @param type
     * @param number
     * @param page
     */
    public void getQianDuanInfo(Subscriber<QianDuanEntity> subscriber, String type, int number, int page) {
        qianDuanServiceObserv = retrofit.create(QianDuanServiceObserv.class);
        qianDuanServiceObserv.getQianDuanInfo(type, number, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取拓展信息
     * @param subscriber
     * @param type
     * @param number
     * @param page
     */
    public void getTuoZhanInfo(Subscriber<TuoZhanEntity> subscriber,String type,int number,int page){
        tuoZhanServiceObserv=retrofit.create(TuoZhanServiceObserv.class);
        tuoZhanServiceObserv.getTuoZhanInfo(type,number,page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
