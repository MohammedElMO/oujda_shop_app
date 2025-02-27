package com.example.oujda_shop.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

public class Alters {


    public static AlertDialog showAlertDialog(Context context ,View dialogView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

         builder.setView(dialogView);


        return builder.create();



    }
}
