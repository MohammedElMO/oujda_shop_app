package com.example.oujda_shop.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static String saveImageToFile(Uri imageUri, Context ctx) {
        try {
            File file = new File(ctx.getExternalFilesDir(null), "product_images");


            if (!file.exists()) {
                file.mkdirs();
            }

            String imagePath = file.getAbsolutePath() + File.separator + "image_" + System.currentTimeMillis() + ".jpg";
            InputStream inputStream = ctx.getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = new FileOutputStream(imagePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            return imagePath;
        } catch (IOException e) {
            Log.e("ImageSave", "Error saving image: " + e.getMessage());
            return null;
        }
    }

}
