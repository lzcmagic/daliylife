package com.lzc.daliylife.di;

import android.util.Log;


import com.lzc.daliylife.di.anno.qulifier.BlueCloth;
import com.lzc.daliylife.di.anno.qulifier.RedCloth;
import com.lzc.daliylife.di.anno.scope.PerActivity;
import com.lzc.daliylife.di.bean.Cloth;
import com.lzc.daliylife.di.bean.Cloths;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class DaggerTestModule {

    @PerActivity
    @Provides
    @RedCloth
    static Cloth getRedCloth(){
        Log.d("lzc", "getRedCloth: ");
        Cloth cloth=new Cloth();
        cloth.setDesc("i have red cloth lala");
        return  cloth;
    }

    @Provides
    @BlueCloth
    static Cloth getBlueCloth(){
        Log.d("lzc", "getBlueCloth: ");
        Cloth cloth=new Cloth();
        cloth.setDesc("i hava blue cloth lala");
        return cloth;
    }

    @Provides
    static Cloths getCloths(@RedCloth Cloth cloth){
        return new Cloths(cloth);
    }
}
