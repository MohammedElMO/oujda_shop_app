package com.example.oujda_shop.DAOs;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.oujda_shop.DataBaseHelper;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Product;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.utils.DateUtility;
import com.example.oujda_shop.utils.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProductQueries implements GeniricDao<Product> {

    Tables table_name;
    DataBaseHelper db;
    Context ctx;

    public ProductQueries(Tables table_name, Context ctx) {
        this.table_name = table_name;
        this.db = DataBaseHelper.getInstance(ctx);
        this.ctx = ctx;
    }

    @Override
    public void insert(Product entity) {
        Log.d("category from db", entity.getCategory().getId() + "");
        ContentValues entityValues = new ContentValues();
        entityValues.put("name", entity.getName());
        entityValues.put("description", entity.getName());
        entityValues.put("imageUrl", entity.getImageUrl());
        entityValues.put("price", entity.getPrice());
        entityValues.put("categoryId", entity.getCategory().getId());
        DbHelper.writeDb(db).insert(this.table_name.name(), null, entityValues);
    }

    public void update(Product entity, Integer id) {
        ContentValues entityValues = new ContentValues();
        entityValues.put("name", entity.getName());
        entityValues.put("description", entity.getName());
        entityValues.put("imageUrl", entity.getImageUrl());
        entityValues.put("price", entity.getPrice());
        entityValues.put("categoryId", entity.getCategory().getId());

        DbHelper.writeDb(db).update(this.table_name.name(), entityValues, "id = ?", new String[]{id.toString()});
    }

    public void delete(Integer id) {
        ContentValues entityValues = new ContentValues();
        entityValues.put("id", id);
        DbHelper.writeDb(db).delete(this.table_name.name(), "id = ?", new String[]{id.toString()});
    }


    public ArrayList<Product> getAllOfCategory(Category category) {
        Cursor cursor = DbHelper.readDb(db).query(this.table_name.name(), null,
                "categoryId = ?", new String[]{String.valueOf(category.getId())}, null, null, null);
        ArrayList<Product> products = new ArrayList<>();

        try (cursor) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndexOrThrow("id");
                int nameIndex = cursor.getColumnIndexOrThrow("name");
                int descIndex = cursor.getColumnIndexOrThrow("description");
                int dateIndex = cursor.getColumnIndexOrThrow("createdAt");
                int resIndex = cursor.getColumnIndexOrThrow("imageUrl");
                int priceIndex = cursor.getColumnIndexOrThrow("price");


                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String description = cursor.getString(descIndex);
                String createdAt = cursor.getString(dateIndex);
                String imageResource = cursor.getString(resIndex);
                int price = cursor.getInt(priceIndex);
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(createdAt);

                products.add(new Product(id, name, description, price, imageResource, DateUtility.getTimeAgo(date), category));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return products;


    }

    public ArrayList<Product> getAll() {
        Cursor cursor = DbHelper.readDb(db).query(this.table_name.name(), null,
                null, null, null, null, null);
        ArrayList<Product> products = new ArrayList<>();

        try (cursor) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndexOrThrow("id");
                int nameIndex = cursor.getColumnIndexOrThrow("name");
                int descIndex = cursor.getColumnIndexOrThrow("description");
                int dateIndex = cursor.getColumnIndexOrThrow("createdAt");
                int resIndex = cursor.getColumnIndexOrThrow("imageUrl");
                int categoryIdIndex = cursor.getColumnIndexOrThrow("categoryId");
                int priceIndex = cursor.getColumnIndexOrThrow("price");

                Log.d("row", idIndex + "");
                Log.d("row", nameIndex + "");
                Log.d("row", dateIndex + "");
                Log.d("row", resIndex + "");

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String description = cursor.getString(descIndex);
                String createdAt = cursor.getString(dateIndex);
                String imageResource = cursor.getString(resIndex);
                int categoryId = cursor.getInt(categoryIdIndex);
                int price = cursor.getInt(priceIndex);
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(createdAt);

                CategoriesQueries queries = new CategoriesQueries(Tables.Category, this.ctx);
                Category category = queries.findById(categoryId);
                products.add(new Product(id, name, description, price, imageResource, DateUtility.getTimeAgo(date), category));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return products;


    }

}

