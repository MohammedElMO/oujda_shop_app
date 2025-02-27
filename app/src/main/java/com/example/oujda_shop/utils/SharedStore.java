package com.example.oujda_shop.utils;


import android.content.Context;
import android.content.SharedPreferences;


public class SharedStore {
    public static String STORE_NAME = "app-store";
    private static SharedStore SharedStoreInstance;
    private SharedPreferences store;


    private SharedPreferences.Editor editor;


    public SharedStore(Context context) {
        store = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
        editor = store.edit();
    }

    // Save data to SharedPreferences
    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();  // or editor.commit() for synchronous operation
    }

    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Retrieve data from SharedPreferences
    public String getString(String key, String defaultValue) {
        return store.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return store.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return store.getBoolean(key, defaultValue);
    }

    // Clear all data
    public void clear() {
        editor.clear();
        editor.apply();
    }

    // Remove a single key-value pair
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }




public static SharedStore getOneStore(Context ctx) {
    if (SharedStoreInstance == null) {
        SharedStoreInstance = new SharedStore(ctx);
    }

    return SharedStoreInstance;

}


}