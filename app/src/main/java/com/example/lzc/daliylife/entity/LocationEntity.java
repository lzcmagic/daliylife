package com.example.lzc.daliylife.entity;

/**
 * Created by lzc on 2017/3/18.
 */

public class LocationEntity {
    String city;
    String province;
    //纬度
    String posLat;
    //经度
    String posLng;

    @Override
    public String toString() {
        return "LocationEntity{" +
                "city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", posLat='" + posLat + '\'' +
                ", posLng='" + posLng + '\'' +
                '}';
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPosLat() {
        return posLat;
    }

    public void setPosLat(String posLat) {
        this.posLat = posLat;
    }

    public String getPosLng() {
        return posLng;
    }

    public void setPosLng(String posLng) {
        this.posLng = posLng;
    }
}
