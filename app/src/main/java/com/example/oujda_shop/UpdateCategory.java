package com.example.oujda_shop;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.DAOs.CategoriesQueries;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.utils.ImageUtils;
import com.example.oujda_shop.utils.InputUtils;
import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.Toaster;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;

public class UpdateCategory extends AppCompatActivity {

    private Uri imageUri;
    static final int REQUEST_IMAGE_PICK = 1;

    EditText categoryName, categoryDescription;
    ImageView categoryImage;
    CategoriesQueries db;
    Button updateCategoryBtn, uploadIcon;
    Category updatedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpActionBar();
        Intent addIntent = getIntent();
        updatedCategory = (Category) addIntent.getSerializableExtra("category");

        categoryImage = findViewById(R.id.categoryImage);
        categoryName = findViewById(R.id.name);
        categoryDescription = findViewById(R.id.description);

        updateCategoryBtn = findViewById(R.id.add_category_btn);
        uploadIcon = findViewById(R.id.add_icon_btn);

        db = new CategoriesQueries(Tables.Category, getApplicationContext());

        categoryName.setText(updatedCategory.getName());
        categoryDescription.setText(updatedCategory.getDescription());

        updateCategoryBtn.setOnClickListener(v -> {

            onUpdateCategory();
        });


        if (!TextUtils.isEmpty(updatedCategory.getImageResource())) {
            File imgFile = new File(updatedCategory.getImageResource());
            if (imgFile.exists()) {
                Uri existingUri = Uri.fromFile(imgFile);
                categoryImage.setImageURI(existingUri);

                imageUri = existingUri;
            }
        }

        uploadIcon.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(uploadIntent, REQUEST_IMAGE_PICK);

        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);

            NavigationUtils.redirectWithAnimation(this, MainActivity.class, options.toBundle());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                categoryImage.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onUpdateCategory() {
        String name = InputUtils.getFieldValue(categoryName);
        String description = InputUtils.getFieldValue(categoryDescription);

        if (imageUri == null) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "you should upload an image for the product", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;

        }
        if (name.trim().isEmpty()) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "you should provide a name for the product", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }


        try {
            String imagePath =  ImageUtils.saveImageToFile(imageUri,getApplicationContext());

            db.update(new Category(name, description, imagePath), updatedCategory.getId());
            NavigationUtils.redirect(this, MainActivity.class);
            Toaster.showSnackBar(this, findViewById(android.R.id.content), "the category was modified with success!", R.drawable.done, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_FADE, R.color.white, R.color.black);

        } catch (Exception e) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "Error", R.drawable.error);
        }
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.action_bar_category_update);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
}