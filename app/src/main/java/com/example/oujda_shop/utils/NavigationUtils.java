package com.example.oujda_shop.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

public class NavigationUtils {


    static public void redirect(Context ctx, Class<?> clsTo) {
        Intent location = new Intent(ctx, clsTo);
        ctx.startActivity(location);
    }


    static public void redirectWithPayload(Context ctx, Class<?> clsTo, Serializable entity, String key) {
        Intent location = new Intent(ctx, clsTo);
        location.putExtra(key, entity);
        ctx.startActivity(location);
    }

    static public void redirectWithAnimation(Context ctx, Class<?> clsTo, Bundle options) {
        Intent location = new Intent(ctx, clsTo);
        ctx.startActivity(location, options);
    }


}
