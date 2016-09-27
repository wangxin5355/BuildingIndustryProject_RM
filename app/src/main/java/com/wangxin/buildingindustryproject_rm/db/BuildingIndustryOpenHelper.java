package com.wangxin.buildingindustryproject_rm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangxin on 2016/7/3.
 */
public class BuildingIndustryOpenHelper extends SQLiteOpenHelper {
    /**
     * 雇员表建表语句
     */
    public static final String CREATE_EMPLOYESES = "create table employees("
            + "id integer primary key autoincrement, "
            + "province_name text, "
            + "province_code text)";
    private Context mContext;
    public BuildingIndustryOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYESES); // 创建雇员表

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_EMPLOYESES);
            case 2:
                db.execSQL("alter table Book add column category_id integer");
            default:
        }

    }
}
