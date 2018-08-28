package com.lzc.daliylife.di.bean;

import javax.inject.Inject;

public class Cloth {

    private String desc;

    @Inject
    public Cloth() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
