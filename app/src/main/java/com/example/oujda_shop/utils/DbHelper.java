package com.example.oujda_shop.utils;

import android.database.sqlite.SQLiteDatabase;

import com.example.oujda_shop.DataBaseHelper;

public class DbHelper {

   public  static SQLiteDatabase writeDb(DataBaseHelper db) {
        return db.getWritableDatabase();
    }

    public static SQLiteDatabase readDb(DataBaseHelper db) {
        return db.getReadableDatabase();
    }
}
