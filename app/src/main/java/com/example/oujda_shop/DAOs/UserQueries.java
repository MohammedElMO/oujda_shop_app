package com.example.oujda_shop.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.oujda_shop.DataBaseHelper;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.entities.User;
import com.example.oujda_shop.utils.DbHelper;

public class UserQueries implements GeniricDao<User> {
    Tables table_name;
    DataBaseHelper db;

    public UserQueries(Tables table_name, Context ctx) {
        this.table_name = table_name;
        this.db = DataBaseHelper.getInstance(ctx);

    }

    public void updateUserPassword(int userId, String newPassword) {
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        DbHelper.writeDb(db).update("User", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    public void updateUserDetails(int userId, String fullName, String email) {
        String[] nameParts = fullName.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("email", email);

        DbHelper.writeDb(db).update("User", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    public User loadUserData(int userId) {
        Cursor cursor = DbHelper.readDb(db).query("User", new String[]{"firstName", "lastName", "email", "profileImage"}, "id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        User u = null;

        if (cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex("firstName");
            int lastNameIndex = cursor.getColumnIndex("lastName");
            int mailIndex = cursor.getColumnIndex("email");
            int profileIndex = cursor.getColumnIndex("profileImage");


            u = new User(cursor.getString(firstNameIndex), cursor.getString(lastNameIndex), cursor.getString(mailIndex), cursor.getString(profileIndex));

            cursor.close();
        }
        return u;
    }

    public void updateProfileImage(int userId, String imagePath) {
        ContentValues values = new ContentValues();
        values.put("profileImage", imagePath);

        DbHelper.writeDb(db).update("User", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    public void updatePassword(int userId, String newPassword) {
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        DbHelper.writeDb(db).update("User", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    public boolean login(String email, String password) {
        Cursor cursor = DbHelper.readDb(db).rawQuery("SELECT id FROM User WHERE email = ? AND password = ?", new String[]{email, password});
        Integer id = null;

        try (cursor) {

            if (cursor.moveToFirst()) {
                id = cursor.getColumnIndex("id");
            }
        }


        return id != null;


    }

    @Override
    public void insert(User entity) {
        ContentValues entityValues = new ContentValues();

        entityValues.put("firstName", entity.getFirstName());
        entityValues.put("lastName", entity.getLastName());
        entityValues.put("email", entity.getEmail());
        entityValues.put("password", entity.getPassword());
        DbHelper.writeDb(db).insert(this.table_name.name(), null, entityValues);


    }

    public boolean findByEmail(String email) {

        Cursor cursor = DbHelper.writeDb(db).rawQuery("SELECT id FROM User WHERE email = ?", new String[]{email});
        try (cursor) {
            return cursor.moveToFirst();
        }

    }

    public int findUserByEmail(String email) {

        Cursor cursor = DbHelper.writeDb(db).rawQuery("SELECT id FROM User WHERE email = ?", new String[]{email});
        int userId = -1;
        try (cursor) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("id");
                userId = cursor.getInt(index);
            }

        }
        return userId;
    }

}