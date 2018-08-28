package com.lzc.daliylife.di.bean;

public class Cloths {
    private Cloth cloth;

    public Cloths(Cloth cloth) {
        this.cloth = cloth;
    }

    public Cloth getCloth() {
        return cloth;
    }

    public void setCloth(Cloth cloth) {
        this.cloth = cloth;
    }

    @Override
    public String toString() {
        return cloth.getDesc()+"衣服";
    }
}
