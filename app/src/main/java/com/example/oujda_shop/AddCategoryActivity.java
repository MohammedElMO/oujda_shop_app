package com.example.oujda_shop;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AddCategoryActivity extends AppCompatActivity {

    Spinner icons;
    EditText categoryName, categoryDescription;
    CategoriesQueries db;
    Button addCategoryBtn;

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
        icons = findViewById(R.id.icons);
        addCategoryBtn = findViewById(R.id.add_category_btn);
        db = new CategoriesQueries(Tables.Category, getApplicationContext());
//        onSelectIcon();

        addCategoryBtn.setOnClickListener(v -> {

            onAddCategory();
        });


    }

    private void onSelectIcon() {
        Integer[] items = {R.drawable.chaire, R.drawable.cosy, R.drawable.furniture1, R.drawable.king_bad};

        // Create an ArrayAdapter
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        // Set the layout for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        icons.setAdapter(adapter);

        // Set a listener for the Spinner to handle item selection
        icons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // You can access the selected item here
                String selectedItem = parentView.getItemAtPosition(position).toString();
//                Toast.makeText(this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // You can perform an action if nothing is selected
            }
        });
    }

    void onAddCategory() {
        String name = InputUtils.getFieldValue(categoryName);
        String description = InputUtils.getFieldValue(categoryDescription);

        if (name.trim().isEmpty()) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "il faut fournir un nom à la catégorie", R.drawable.info_icon_blue, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_SLIDE, R.color.white, R.color.black);
            return;
        }

        try {
            db.insert(new Category(name, description, null));
            NavigationUtils.redirect(this, MainActivity.class);
        } catch (Exception e) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "errur lors de l'insertion de categorie", R.drawable.error);
        }

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