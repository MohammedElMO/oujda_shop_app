package com.example.oujda_shop.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.oujda_shop.DataBaseHelper;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.utils.DateUtility;
import com.example.oujda_shop.utils.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CategoriesQueries implements GeniricDao<Category> {

    Tables table_name;
    DataBaseHelper db;

    public CategoriesQueries(Tables table_name, Context ctx) {
        this.table_name = table_name;
        this.db = DataBaseHelper.getInstance(ctx);

    }

    @Override
    public void insert(Category entity) {
        ContentValues entityValues = new ContentValues();
        entityValues.put("name", entity.getName());
        entityValues.put("description", entity.getName());
        entityValues.put("imageUrl", entity.getImageResource());
        DbHelper.writeDb(db).insert(this.table_name.name(), null, entityValues);
    }

    public void update(Category entity, Integer id) {
        ContentValues entityValues = new ContentValues();
        entityValues.put("name", entity.getName());
        entityValues.put("description", entity.getName());
        entityValues.put("imageUrl", entity.getImageResource());

        DbHelper.writeDb(db).update(this.table_name.name(), entityValues, "id = ?", new String[]{id.toString()});
    }

    public void delete(Integer id) {
        ContentValues entityValues = new ContentValues();
        entityValues.put("id", id);
        DbHelper.writeDb(db).delete(this.table_name.name(), "id = ?", new String[]{id.toString()});
    }

    public Category findById(Integer id) {
        Cursor cursor = DbHelper.readDb(db).query(this.table_name.name(), null,
                "id = ?", new String[]{id.toString()}, null, null, null);

        Category foundCategory = null;
        try (cursor) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow("id");
                int nameIndex = cursor.getColumnIndexOrThrow("name");
                int dateIndex = cursor.getColumnIndexOrThrow("createdAt");
                int resIndex = cursor.getColumnIndexOrThrow("imageUrl");

                foundCategory = new Category(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getString(resIndex), cursor.getString(dateIndex));
            }
        }

        return foundCategory;
    }

    public Category findByName(String name) {
        Cursor cursor = DbHelper.readDb(db).query(this.table_name.name(), null,
                "name = ?", new String[]{name}, null, null, null);

        Category foundCategory = null;
        try (cursor) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow("id");
                int nameIndex = cursor.getColumnIndexOrThrow("name");
                int dateIndex = cursor.getColumnIndexOrThrow("createdAt");
                int resIndex = cursor.getColumnIndexOrThrow("imageUrl");

                foundCategory = new Category(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getString(resIndex), cursor.getString(dateIndex));
            }
        }

        return foundCategory;
    }


    public ArrayList<Category> getAll() {
        Cursor cursor = DbHelper.readDb(db).query(this.table_name.name(), new String[]{"id", "name", "createdAt", "imageUrl"},
                null, null, null, null, null);
        ArrayList<Category> categories = new ArrayList<>();

        try (cursor) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndexOrThrow("id");
                int nameIndex = cursor.getColumnIndexOrThrow("name");
                int dateIndex = cursor.getColumnIndexOrThrow("createdAt");
                int resIndex = cursor.getColumnIndexOrThrow("imageUrl");



                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String createdAt = cursor.getString(dateIndex);
                String imageResource = cursor.getString(resIndex);
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(createdAt);

                categories.add(new Category(id, name, imageResource, DateUtility.getTimeAgo(date)));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return categories;


    }

}
