package com.lzc.daliylife.di;


import com.lzc.daliylife.di.bean.Cloth;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class DaggerTestModule2 {

    @Provides
    static Cloth getCloths(){
        Cloth cloth = new Cloth();
        cloth.setDesc("second red cloth");
        return cloth;
    }
}
