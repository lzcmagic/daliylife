package com.example.lzc.retrofitandrxjavatest.sqlentity;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by lzc on 2016/12/7.
 */
@Database(name = AppDatabase.NAME,version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME="AppDatabase";
    public static final int VERSION=1;
}
