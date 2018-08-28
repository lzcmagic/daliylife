package com.lzc.daliylife.di.base;

import android.app.Application;
import android.content.Context;


import com.lzc.daliylife.di.bean.ClothHandle;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MyAppModule {
    @Singleton
    @Binds
    abstract Context provideContext(Application application);

    @Singleton
    @Provides
    static ClothHandle getClothHandle(Context context){
        return new ClothHandle(context);
    }

}
