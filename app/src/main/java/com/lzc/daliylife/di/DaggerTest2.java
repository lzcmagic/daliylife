package com.lzc.daliylife.di;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import com.lzc.daliylife.R;
import com.lzc.daliylife.di.bean.Cloth;
import com.lzc.daliylife.di.bean.ClothHandle;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public class DaggerTest2 extends DaggerAppCompatActivity {



    @Inject
    Cloth cloth;

    @Inject
    ClothHandle clothHandle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);
        TextView textView= (TextView) findViewById(R.id.textView2);
        findViewById(R.id.button).setVisibility(View.GONE);

        textView.setText(cloth.toString()+" "+clothHandle.toString()+" context: "+clothHandle.handle(cloth));

    }
}
