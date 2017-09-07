package com.lzc.daliylife.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lzc.daliylife.normalUtil.L;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzc on 2017/9/7.
 *
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder bind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        bind = ButterKnife.bind(this);
        initPresenter();
        initUI();
        L.d("onCreate");
    }


    public abstract void initUI();

    /**
     * 初始化P
     */
    public abstract void initPresenter();

    /**
     * 获取布局资源id
     * @return 布局资源id
     */
    public abstract int getResId();

    @Override
    protected void onStart() {
        super.onStart();
        L.d("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        L.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        L.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.d("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d("onDestroy");
        if (bind!=null){
            bind.unbind();
        }
    }
}
