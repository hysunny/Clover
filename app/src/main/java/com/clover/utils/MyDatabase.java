package com.clover.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YUSHAN on 2015/7/2.
 */
public class MyDatabase extends SQLiteOpenHelper {

    public MyDatabase(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
        // TODO 自动生成的构造函数存根
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists myrecord(bestscore int)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 自动生成的方法存根

    }

}
