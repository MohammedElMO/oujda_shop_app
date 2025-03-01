package com.example.oujda_shop;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.DAOs.CategoriesQueries;
import com.example.oujda_shop.DAOs.ProductQueries;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Product;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.utils.ImageUtils;
import com.example.oujda_shop.utils.InputUtils;
import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.Toaster;
import com.google.android.material.snackbar.Snackbar;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UpdateProduct extends AppCompatActivity {
    Spinner categoriesSpinner;
    EditText productName, productDescription, productPrice;
    ProductQueries productDb;
    Category selectedCategory;
    Button updateProductBtn;
    Product selectedProduct;
    ImageView productImage;
    Button uploadBtn;


    private Uri imageUri;
    static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpActionBar();
        categoriesSpinner = findViewById(R.id.categories);
        productName = findViewById(R.id.name);
        productDescription = findViewById(R.id.description);
        productPrice = findViewById(R.id.price);
        updateProductBtn = findViewById(R.id.add_product_btn);
        productImage = findViewById(R.id.productImage);
        uploadBtn = findViewById(R.id.upload_image_btn);

        Intent myIntent = getIntent();

        selectedProduct = (Product) myIntent.getSerializableExtra("product");


        productName.setText(selectedProduct.getName());
        productDescription.setText(selectedProduct.getDescription());
        productPrice.setText(String.valueOf(selectedProduct.getPrice()));
        categoriesSpinner.setSelection(selectedProduct.getCategory().getId());


        if (!TextUtils.isEmpty(selectedProduct.getImageUrl())) {
            File imgFile = new File(selectedProduct.getImageUrl());
            if (imgFile.exists()) {
                Uri existingUri = Uri.fromFile(imgFile);
                productImage.setImageURI(existingUri);

                imageUri = existingUri;
            }
        }

        productDb = new ProductQueries(Tables.Product, getApplicationContext());

        updateProductBtn.setOnClickListener(v -> {
            onUpdateProduct();
        });

        uploadBtn.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(uploadIntent, REQUEST_IMAGE_PICK);

        });
        onSelect();


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                productImage.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSelect() {
        CategoriesQueries categoriesQueries = new CategoriesQueries(Tables.Category, getApplicationContext());

        ArrayList<Category> categoryList = categoriesQueries.getAll();
        ArrayList<String> categoryNames = new ArrayList<>();

        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);

        int categoryIndex = -1;
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == selectedProduct.getCategory().getId()) {
                categoryIndex = i;
                selectedCategory = categoryList.get(i);
                break;
            }
        }

        if (categoryIndex != -1) {
            categoriesSpinner.setSelection(categoryIndex);
        }

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoryList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }
        });
    }

    void onUpdateProduct() {
        String name = InputUtils.getFieldValue(productName);
        String description = InputUtils.getFieldValue(productDescription);
        String price = InputUtils.getFieldValue(productPrice);

        if (imageUri == null) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "you should upload an image for the product", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;

        }
        if (name.trim().isEmpty()) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "product name must be provided", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }

        if (price.trim().isEmpty()) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "price must be provided", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }

        if (selectedCategory == null) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "you should select a category", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }
        String imagePath =  ImageUtils.saveImageToFile(imageUri,getApplicationContext());


        try {
            productDb.update(new Product(name, description, Double.parseDouble(price), imagePath, selectedCategory),selectedProduct.getId());
            NavigationUtils.redirectWithPayload(this, ProductActivity.class,selectedProduct.getCategory(),"category");
        } catch (Exception e) {
            e.printStackTrace();
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "Error", R.drawable.error);
        }

    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            Intent location =  new Intent(this,ProductActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);
            location.putExtra("category",selectedProduct.getCategory());

            startActivity(location,options.toBundle());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.action_bar_update_product);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
}