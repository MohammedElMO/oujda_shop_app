package com.example.oujda_shop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static DataBaseHelper DbInstance = null;


    private DataBaseHelper(@Nullable Context ctx) {
        super(ctx, "shop.db", null, 14);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS User (" +
                "id INTEGER  PRIMARY KEY AUTOINCREMENT," +
                "firstName TEXT," +
                "lastName TEXT," +
                "profileImage String ," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL); "
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Category (" +
                        "id INTEGER  PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL UNIQUE," +
                        "description TEXT," +
                        "imageUrl TEXT  ," +
                        "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP); "
        );
        db.execSQL(
                "CREATE TABLE  IF NOT EXISTS Product (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "description TEXT," +
                        "price REAL NOT NULL ," +
                        "imageUrl TEXT NOT NULL," +
                        "categoryId INTEGER," +
                        "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (categoryId) REFERENCES Category(id) ON DELETE CASCADE);"
        );

    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS User" );
        db.execSQL("DROP TABLE IF EXISTS Category" );
        db.execSQL("DROP TABLE IF EXISTS Product" );
        onCreate(db);
    }

    public static synchronized DataBaseHelper getInstance(Context ctx) {
        if (DbInstance == null) {
            DbInstance = new DataBaseHelper(ctx);
        }

        return DbInstance;
    }
}
