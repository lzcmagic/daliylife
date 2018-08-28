package com.lzc.daliylife.di.base;

import android.app.Application;


import com.lzc.daliylife.framework.MyApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


/**
 * @see javax.inject.Qualifier @Qualifier
 * @see javax.inject.Named @Named
 *
 * @see javax.inject.Scope @Scope
 * @see javax.inject.Singleton @Singleton
 *
 * @see dagger.Module @Module ( {@link dagger.Provides} and {@link dagger.Binds})  -> {@link Component} -> {@link javax.inject.Inject}
 */

@Singleton
@Component(modules ={
        AndroidSupportInjectionModule.class,
        MyAppModule.class,
        ActivityBuilder.class})
public interface AppComponent extends AndroidInjector<MyApp>{


    @Override
    void inject(MyApp instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
