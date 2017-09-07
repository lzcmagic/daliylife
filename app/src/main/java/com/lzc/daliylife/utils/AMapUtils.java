package com.lzc.daliylife.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lzc.daliylife.entity.LocationEntity;
import com.lzc.daliylife.framework.ApplWork;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lzc on 2017/3/27.
 */

public class AMapUtils {
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener ;
    private static AMapUtils instance;
    private SendLocation mSendLocation;

    public AMapUtils() {
        initLocationOptions(ApplWork.ApplWorkContext);
    }

    public static AMapUtils getInstance(){
        if (instance==null){
            return instance=new AMapUtils();
        }
        return instance;
    }

    public void startLocation(SendLocation sendLocation){
        this.mSendLocation=sendLocation;
        mLocationClient.startLocation();
    }
    private void initLocationOptions(Context context) {
        mLocationClient = new AMapLocationClient(context);
        mLocationListener = new MyAMapLocationListener();
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(mLocationListener);
    }

    private class MyAMapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    //aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    double latitude = aMapLocation.getLatitude();//获取纬度
                    double longitude = aMapLocation.getLongitude();//获取经度
                    aMapLocation.getAccuracy();//获取精度信息
                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    aMapLocation.getCountry();//国家信息
                    String province = aMapLocation.getProvince();//省信息
                    String city = aMapLocation.getCity();//城市信息
                    aMapLocation.getDistrict();//城区信息
                    aMapLocation.getStreet();//街道信息
                    aMapLocation.getStreetNum();//街道门牌号信息
                    aMapLocation.getCityCode();//城市编码
                    aMapLocation.getAdCode();//地区编码
                    aMapLocation.getAoiName();//获取当前定位点的AOI信息
                    aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                    aMapLocation.getFloor();//获取当前室内定位的楼层
                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    df.format(date);
                    LocationEntity locationEntity=new LocationEntity();
                    locationEntity.setProvince(province);
                    locationEntity.setPosLng(String.valueOf(longitude));
                    locationEntity.setPosLat(String.valueOf(latitude));
                    locationEntity.setCity(city);
                    mSendLocation.sendLocation(locationEntity);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
                if (mLocationClient.isStarted())
                    mLocationClient.stopLocation();
            }
            mSendLocation.sendLocation(null);
        }
    }
    public interface SendLocation{
        void sendLocation(LocationEntity entity);
    }
}
