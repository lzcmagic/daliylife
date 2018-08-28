package com.lzc.daliylife.di.bean;

import android.content.Context;
import android.widget.Toast;

public class ClothHandle {

    private Context context;

    public ClothHandle() {

    }

    public ClothHandle(Context context) {
        this.context = context;
    }

    public Cloths handle(Cloth cloth) {
        if (context != null)
            Toast.makeText(context, "make cloth yeah", Toast.LENGTH_SHORT).show();
        return new Cloths(cloth);
    }
}
