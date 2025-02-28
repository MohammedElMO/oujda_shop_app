package com.example.oujda_shop;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.oujda_shop.utils.InputUtils;
import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.Toaster;
import com.google.android.material.snackbar.Snackbar;

public class UpdateCategory extends AppCompatActivity {

    Spinner icons;
    EditText categoryName, categoryDescription;
    CategoriesQueries db;
    Button updateCategoryBtn;
    Category updatedCategory ;
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
        Intent addIntent = getIntent() ;
        updatedCategory = (Category) addIntent.getSerializableExtra("category");

        categoryName = findViewById(R.id.name);
        categoryDescription = findViewById(R.id.description);
        icons = findViewById(R.id.icons);
        updateCategoryBtn = findViewById(R.id.add_category_btn);
//        onSelectIcon();
        db = new CategoriesQueries(Tables.Category, getApplicationContext());

        categoryName.setText(updatedCategory.getName());
        categoryDescription.setText(updatedCategory.getDescription());
//        icons.setSelection(updatedCategory.getImageResource());

        updateCategoryBtn.setOnClickListener(v -> {

            onUpdateCategory();
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
   public void onUpdateCategory() {
        String name = InputUtils.getFieldValue(categoryName);
        String description = InputUtils.getFieldValue(categoryDescription);

        if (name.trim().isEmpty()) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "il faut fournir un nom à la catégorie", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }

        try {
            db.update(new Category(name, description, null),updatedCategory.getId());
            NavigationUtils.redirect(this, MainActivity.class);
            Toaster.showSnackBar(this, findViewById(android.R.id.content), "la categorie a ete modifiée avec succès", R.drawable.done, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_FADE, R.color.white, R.color.black);

        } catch (Exception e) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "errur lors de l'insertion de categorie", R.drawable.error);
        }
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