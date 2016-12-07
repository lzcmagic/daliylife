package com.example.lzc.retrofitandrxjavatest.sqlentity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by lzc on 2016/12/7.
 */
@Table(database = AppDatabase.class)
public class AffixInfo extends BaseModel {
    @PrimaryKey
    String affixId;
    @Column
    String belongTo;
    @Column
    String affixPath;
}
