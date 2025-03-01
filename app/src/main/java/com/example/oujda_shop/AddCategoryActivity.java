package com.example.oujda_shop;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

import java.io.IOException;

public class AddCategoryActivity extends AppCompatActivity {

    EditText categoryName, categoryDescription;
    CategoriesQueries db;
    Button addCategoryBtn,uploadIcon;
    ImageView categoryImage;
    private Uri imageUri;
    static final int REQUEST_IMAGE_PICK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpActionBar();
        categoryName = findViewById(R.id.name);
        categoryDescription = findViewById(R.id.description);
        uploadIcon = findViewById(R.id.add_icon_btn);
        addCategoryBtn = findViewById(R.id.add_category_btn);
        categoryImage = findViewById(R.id.categoryImage);

        db = new CategoriesQueries(Tables.Category, getApplicationContext());

        uploadIcon.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(uploadIntent, REQUEST_IMAGE_PICK);

        });

        addCategoryBtn.setOnClickListener(v -> {
            onAddCategory();
        });


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


    void onAddCategory() {
        String name = InputUtils.getFieldValue(categoryName);
        String description = InputUtils.getFieldValue(categoryDescription);

        if (name.trim().isEmpty()) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "you should provide a category name", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }
        if (imageUri == null) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "you should upload an icon to the category", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;

        }

        try {
            String imagePath = ImageUtils.saveImageToFile(imageUri, getApplicationContext());

            db.insert(new Category(name, description, imagePath));
            NavigationUtils.redirect(this, MainActivity.class);
        } catch (Exception e) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "Error when inserting category", R.drawable.error);
        }

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);

            NavigationUtils.redirectWithAnimation(this,MainActivity.class,options.toBundle());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.action_bar_category);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
}