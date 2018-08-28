package com.lzc.daliylife.di.base;


import com.lzc.daliylife.di.DaggerTest;
import com.lzc.daliylife.di.DaggerTest2;
import com.lzc.daliylife.di.DaggerTestModule;
import com.lzc.daliylife.di.DaggerTestModule2;
import com.lzc.daliylife.di.anno.scope.PerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {DaggerTestModule2.class})
    abstract DaggerTest2 bindDagger2Activity();

    @PerActivity
    @ContributesAndroidInjector(modules = {DaggerTestModule.class})
    abstract DaggerTest bindDaggerActivity();

}
