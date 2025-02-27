package com.example.oujda_shop.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.oujda_shop.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class Toaster {


    //    (android.R.id.content)
    public static void showSnackBar(Context context, View view, String message,int drawable) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        // Get Snackbar's TextView
        View snackbarView = snackbar.getView();
        TextView snackbarTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

        // Add icon to the left of text
//        com.google.android.material.R.drawable.abc_ab_share_pack_mtrl_alpha
        Drawable icon = ContextCompat.getDrawable(context, drawable); // Replace with your icon
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        snackbarTextView.setCompoundDrawables(icon, null, null, null);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        // Add some spacing
        snackbarTextView.setCompoundDrawablePadding(16);

        snackbar.show();
    }

    public static void showSnackBar(Context context, View view, String message,int drawable, int duration, @BaseTransientBottomBar.AnimationMode int animationMode,int bgColor,int textColor  ) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setDuration(duration | Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(context, bgColor));
        snackbar.setTextColor(ContextCompat.getColor(context,textColor));

        View snackbarView = snackbar.getView();
        TextView snackbarTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

        Drawable icon = ContextCompat.getDrawable(context, drawable);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        snackbarTextView.setCompoundDrawables(icon, null, null, null);
        snackbar.setAnimationMode(animationMode);

        snackbarTextView.setCompoundDrawablePadding(16);

        snackbar.show();
    }
}
