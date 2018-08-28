package com.lzc.daliylife.http;

import android.util.Log;

import com.lzc.daliylife.BuildConfig;
import com.lzc.daliylife.entity.gankentity.AndroidEntity;
import com.lzc.daliylife.entity.gankentity.FuLiEntity;
import com.lzc.daliylife.entity.gankentity.IOSEntity;
import com.lzc.daliylife.entity.gankentity.QianDuanEntity;
import com.lzc.daliylife.entity.gankentity.TuoZhanEntity;
import com.lzc.daliylife.entity.juhe.WechatEntity;
import com.lzc.daliylife.entity.mob.LaoHuangLiEntity;
import com.lzc.daliylife.entity.mob.LotteryEntity;
import com.lzc.daliylife.entity.mob.WeatherEntity;
import com.lzc.daliylife.entity.yiyuan.BSQJEntity;
import com.lzc.daliylife.entity.yiyuan.SummaryLotteryEntity;
import com.lzc.daliylife.framework.MyApp;
import com.lzc.daliylife.normalUtil.NetUtils;
import com.lzc.daliylife.normalUtil.T;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lzc on 2016/11/14.
 *
 */
public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 5;

    private static final int RELEASE_TIMEOUT=8;
    private HttpService httpService;

    //构造方法私有
    private HttpMethods(String url) {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();


        if (BuildConfig.DEBUG){
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
            httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            httpClientBuilder.addInterceptor(loggingInterceptor);

        }else {
            httpClientBuilder.connectTimeout(RELEASE_TIMEOUT, TimeUnit.SECONDS);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        httpService= retrofit.create(HttpService.class);
    }


    //获取单例 饿汉模式
    public static synchronized HttpMethods getInstance(String url) {
        return new HttpMethods(url);
    }


    /**
     * 判断网络是否连接
     * @return true or false
     */
    private boolean IsConnect(){
        boolean connected = NetUtils.isConnected(MyApp.getApplWorkContext());
        if (!connected){
            T.toast("未检测到网络");
        }
        return connected;
    }


    /**
     * get near week weather
     *
     * @param subscriber 观察者对象
     * @param key key
     * @param city city
     * @param province province
     */
    public void getWeekWeather(Observer<WeatherEntity> subscriber, String key, String city, String province) {
        if (!IsConnect()){
            return;
        }
        httpService.getWeekWeather(key, city, province)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取老黄历信息
     *
     * @param subscriber 观察者对象
     * @param key key
     * @param date date
     */
    public void getDayLHL(Observer<LaoHuangLiEntity> subscriber, String key, String date) {
        if (!IsConnect()){
            return;
        }
        httpService.getDayLHL(key, date)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取福利数据
     *
     * @param subscriber 观察者对象
     * @param type type
     * @param number number
     * @param page page
     */
    public void getFuLiInfo(Observer<FuLiEntity> subscriber, String type, int number, int page) {
        if (!IsConnect()){
            return;
        }
        httpService.getFuLiInfo(type, number, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 获取android信息
     *
     * @param subscriber 观察者对象
     * @param type type
     * @param number number
     * @param page page
     */
    public void getAndroidInfo(Observer<AndroidEntity> subscriber, String type, int number, int page) {
        if (!IsConnect()){
            return;
        }
        httpService.getAndroidInfo(type, number, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取IOS信息
     *
     * @param subscribe 观察者对象
     * @param type type
     * @param number number
     * @param page page
     */
    public void getIOSInfo(Observer<IOSEntity> subscribe, String type, int number, int page) {
        if (!IsConnect()){
            return;
        }
        httpService.getIOSInfo(type, number, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscribe);
    }

    /**
     * 获取前端信息
     * @param subscriber 观察者对象
     * @param type type
     * @param number number
     * @param page page
     */
    public void getQianDuanInfo(Observer<QianDuanEntity> subscriber, String type, int number, int page) {
        if (!IsConnect()){
            return;
        }
        httpService.getQianDuanInfo(type, number, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取拓展信息
     * @param subscriber 观察者对象
     * @param type type
     * @param number number
     * @param page page
     */
    public void getTuoZhanInfo(Observer<TuoZhanEntity> subscriber,String type,int number,int page){
        if (!IsConnect()){
            return;
        }
        httpService.getTuoZhanInfo(type,number,page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取彩票信息
     * @param subscriber 观察者对象
     * @param key key
     * @param name name
     */
    public void getLotteryInfo(Observer<LotteryEntity> subscriber, String key, String name){
        if (!IsConnect()){
            return;
        }
        httpService.getLotteryInfo(key,name)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取彩票信息
     * @param subscriber 观察者对象
     * @param pno pno
     * @param ps ps
     * @param dtype 彩票种类
     */
    public void getWechatInfo(Observer<WechatEntity> subscriber, int pno, int ps,String dtype,String key){
        if (!IsConnect()){
            return;
        }
        httpService.getWechatInfo(pno,ps,dtype,key)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getYYLotteryInfo(Observer<SummaryLotteryEntity> observer,String path, Map<String,String> map){
        if (!IsConnect()){
            return;
        }
        httpService.getYiYuanLotteryInfo(path,map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getBSQJInfo(Observer<BSQJEntity> observer, String path, Map<String,String> map){
        if (!IsConnect()){
            return;
        }
        httpService.getBSQJInfo(path,map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
