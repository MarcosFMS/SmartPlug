package com.iot.smartplug.smartplug.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marcos on 26/03/2017.
 */

public class SmartplugDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Smartplug.db";
    private static SmartplugDbHelper dbHelper;

    private SmartplugDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDAO.CREATE_TABLE);
        db.execSQL(DeviceDAO.CREATE_TABLE);
        db.execSQL(UserControlsDeviceDAO.CREATE_TABLE);
    }

    public static SmartplugDbHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new SmartplugDbHelper(context);
        }
        return dbHelper;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(UserDAO.DROP_TABLE);
        db.execSQL(DeviceDAO.DROP_TABLE);
        db.execSQL(UserControlsDeviceDAO.DROP_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
