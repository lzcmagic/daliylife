package com.example.lzc.retrofitandrxjavatest.sqlentity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by lzc on 2016/12/7.
 */
@Table(database = AppDatabase.class)
public class UserInfo extends BaseModel {
    @PrimaryKey(autoincrement = true)
    long userId;
    @Column
    String userName;
    @Column
    String passWord;
    @Column
    String affixId;
}
