package com.lzc.daliylife.di;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.lzc.daliylife.R;
import com.lzc.daliylife.di.anno.qulifier.BlueCloth;
import com.lzc.daliylife.di.anno.qulifier.RedCloth;
import com.lzc.daliylife.di.bean.Cloth;
import com.lzc.daliylife.di.bean.ClothHandle;
import com.lzc.daliylife.di.bean.Cloths;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;


public class DaggerTest extends DaggerAppCompatActivity {


    @Inject
    @BlueCloth
    Lazy<Cloth> blueCloth;

    @Inject
    @RedCloth
    Provider<Cloth> redCloth;

    @Inject
    Cloths cloths;

    @Inject
    ClothHandle clothHandle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);
        TextView textView= (TextView) findViewById(R.id.textView2);
        Log.d("lzc", "inject done ...");
        Log.d("lzc", "1 use redCloth instance ..");
        Log.d("lzc", "redCloth:" + redCloth.get());
        Log.d("lzc", "2 use redCloth instance ..");
        Log.d("lzc", "redCloth:" + redCloth.get());
        Log.d("lzc", "1 use blueCloth instance ..");
        Log.d("lzc", "shoe:" + blueCloth.get());
        Log.d("lzc", "2 use blueCloth instance ..");
        Log.d("lzc", "shoe:" + blueCloth.get());

        StringBuilder sb=new StringBuilder();
        String format = String.format("%s %s", blueCloth.get().getDesc(), redCloth.get().getDesc());
        sb.append(format);
        sb.append("\n");
        sb.append(cloths.getCloth().getDesc());
        sb.append("\n");
        sb.append("redCloth is equals to Cloths.getCloth: ").append(cloths.getCloth() == redCloth.get());
        sb.append("\n");
        sb.append(String.format("clothHandle: %s", clothHandle.toString()));
        textView.setText(sb.toString());
    }

    public void intent(View view){
        startActivity(new Intent(this,DaggerTest2.class));
    }
}
