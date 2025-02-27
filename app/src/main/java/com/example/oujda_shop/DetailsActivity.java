package com.example.oujda_shop;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.DAOs.CategoriesQueries;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Product;
import com.example.oujda_shop.entities.Tables;

import java.io.File;

public class DetailsActivity extends AppCompatActivity {
    Product product ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpActionBar();
        Intent myIntent = getIntent();
        product = (Product) myIntent.getSerializableExtra("product");


        TextView description = findViewById(R.id.product_description);
        TextView name = findViewById(R.id.product_name);
        TextView price = findViewById(R.id.product_price);
        TextView category = findViewById(R.id.category_badge);
        ImageView image = findViewById(R.id.product_image);


        category.setText(product.getCategory().getName());
        price.setText(String.valueOf(product.getPrice()) + " DH");
        description.setText(product.getDescription());
        name.setText(product.getName());
        String imagePath = product.getImageUrl();
        File imgFile = new File(imagePath);

        if (imgFile.exists()) {
            image.setImageURI(Uri.fromFile(imgFile));
        } else {
            image.setImageResource(R.drawable.oujda_shop);
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            Intent location =  new Intent(this,ProductActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);
            location.putExtra("category",product.getCategory());

            startActivity(location,options.toBundle());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.action_bar_product_details);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
}