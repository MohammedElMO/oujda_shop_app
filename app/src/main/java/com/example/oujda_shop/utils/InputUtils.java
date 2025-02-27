package com.example.oujda_shop.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.oujda_shop.R;

public class InputUtils {
    public static String getFieldValue(EditText field) {
        return field.getText().toString();
    }

    public static void switchVisibility(ImageView icon, Context context, EditText passwordField) {
        Drawable visibleIcon = ContextCompat.getDrawable(context, R.drawable.invisible_eye);
        Drawable invisibleIcon = ContextCompat.getDrawable(context, R.drawable.visible_eye);


        final boolean[] isPasswordVisible = {false};

        icon.setOnClickListener(v -> {
            Log.d("hhe", "clickign the icon");

            if (isPasswordVisible[0]) {
                icon.setImageDrawable(visibleIcon);
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                Log.d(isPasswordVisible[0] + "", "clickign the icon");

            } else {
                icon.setImageDrawable(invisibleIcon);
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                Log.d(isPasswordVisible[0] + "", "clickign the icon");

            }
            isPasswordVisible[0] = !isPasswordVisible[0];
            passwordField.setSelection(passwordField.getText().length());

        });


    }
}
