package com.example.oujda_shop;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.DAOs.ProductQueries;
import com.example.oujda_shop.adapter.ProductAdapter;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Product;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.utils.Alters;
import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.Toaster;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private GridView gridView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    private ImageButton addProductBtn;
    private ProductQueries productDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpActionBar();
        productDb = new ProductQueries(Tables.Product, getApplicationContext());
        gridView = findViewById(R.id.productsGrid);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("category");


        productList = productDb.getAllOfCategory(category);


        productAdapter = new ProductAdapter(this, productList);
        gridView.setAdapter(productAdapter);


        addProductBtn.setOnClickListener(v -> {
            NavigationUtils.redirectWithPayload(this, AddProductActivity.class, category, "category");
        });

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Product p = (Product) parent.getItemAtPosition(position);
            NavigationUtils.redirectWithPayload(this, DetailsActivity.class, p, "product");
        });

        gridView.setOnItemLongClickListener((parent, view, position, id) -> {
            Product p = (Product) parent.getItemAtPosition(position);

            showConfirmation(p);

            return true;
        });

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

    private void showConfirmation(Product product) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_dialog, null);

        Button positiveButton = dialogView.findViewById(R.id.button_positive);
        Button negativeButton = dialogView.findViewById(R.id.button_negative);

        AlertDialog dialog = Alters.showAlertDialog(this, dialogView);

        positiveButton.setOnClickListener(v -> {
                    NavigationUtils.redirectWithPayload(this, UpdateProduct.class, product, "product");
                    dialog.dismiss();
                }
        );

        negativeButton.setOnClickListener(v -> {
            productDb.delete(product.getId());
            productAdapter.updateList(productDb.getAll());
            dialog.dismiss();
            Toaster.showSnackBar(this, findViewById(android.R.id.content), "le produit a ete supprimer", R.drawable.done, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_FADE, R.color.white, R.color.black);

        });

        dialog.show();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.action_bar_product);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            View customView = actionBar.getCustomView();

            // Find the button inside the custom view
            addProductBtn = customView.findViewById(R.id.add_product_btn);


        }



    }
}