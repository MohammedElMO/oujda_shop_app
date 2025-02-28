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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.DAOs.ProductQueries;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Product;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.utils.ImageUtils;
import com.example.oujda_shop.utils.InputUtils;
import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.Toaster;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class AddProductActivity extends AppCompatActivity {

    EditText productName, productPrice;
    EditText productDescription;
    ProductQueries db;
    Button addProductBtn;
    Button uploadBtn;
    Category selectedCategory;
    ImageView productImage;

    private Uri imageUri;
    static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpActionBar();
        productName = findViewById(R.id.name);
        productDescription = findViewById(R.id.description);


        addProductBtn = findViewById(R.id.add_product_btn);

        productPrice = findViewById(R.id.price);
        uploadBtn = findViewById(R.id.upload_image_btn);
        productImage = findViewById(R.id.productImage);

        db = new ProductQueries(Tables.Product, getApplicationContext());

        Intent intent = getIntent();

        selectedCategory = (Category) intent.getSerializableExtra("category");

        uploadBtn.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(uploadIntent, REQUEST_IMAGE_PICK);

        });

        addProductBtn.setOnClickListener(v -> {
            onAddProduct();
        });


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

    public void onAddProduct() {
        String name = productName.getText().toString();
        String description = (productDescription).getText().toString();
        String price = InputUtils.getFieldValue(productPrice);


        if (imageUri == null) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "tu doit selectionner un image au produit", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;

        }

        if (selectedCategory == null) {
            return;
        }


        if (name.trim().isEmpty()) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "nom de produit est obligatoire", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }

        if (price.trim().isEmpty() || price.trim().isBlank()) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "le prix est obligatoire", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }
        if (!TextUtils.isDigitsOnly(price)) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "le prix doit etre un nombre", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }

        try {
            String imagePath = ImageUtils.saveImageToFile(imageUri, getApplicationContext());

            Product p = new Product(name, description, Double.parseDouble(price), imagePath, selectedCategory);

            db.insert(p);
            NavigationUtils.redirectWithPayload(this, ProductActivity.class, selectedCategory, "category");
        } catch (Exception e) {
            e.printStackTrace();
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "errur lors de l'insertion de produit", R.drawable.error);
        }

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);
           Intent intent = new Intent(this, ProductActivity.class);
            intent.putExtra("category", selectedCategory);

            startActivity(intent,options.toBundle());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.action_bar_create_product);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);


        }

    }
}