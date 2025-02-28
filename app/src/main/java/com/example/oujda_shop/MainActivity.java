package com.example.oujda_shop;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.DAOs.CategoriesQueries;
import com.example.oujda_shop.DAOs.UserQueries;
import com.example.oujda_shop.adapter.CategoryAdapter;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.entities.User;
import com.example.oujda_shop.utils.Alters;
import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.SharedStore;
import com.example.oujda_shop.utils.Toaster;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    // ML kit - > access to camera + scan code QR + show product details
//    Log.d("Hello","Dbrna 3liha");

    ListView categoriesList;
    Button addNewCategoryBtn;
    CategoriesQueries db;
    ArrayList<Category> categories;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addNewCategoryBtn = findViewById(R.id.add_new_category_btn);
        db = new CategoriesQueries(Tables.Category, getApplicationContext());
//
        setUpActionBar();
        onAddNewCategory();
//        //TODO: gray color for the icons of the list
        categoriesList = findViewById(R.id.categories_list);
        categories = db.getAll();
        adapter = new CategoryAdapter(this, categories);
        categoriesList.setAdapter(adapter);

        categoriesList.setOnItemClickListener((parent, view, position, id) -> {
            Category category = (Category) parent.getItemAtPosition(position);

            NavigationUtils.redirectWithPayload(this, ProductActivity.class, category, "category");

        });
        categoriesList.setOnItemLongClickListener((parent, view, position, id) -> {
            Category category = (Category) parent.getItemAtPosition(position);
            showConfirmation(category);

            return true;
        });


    }

    private void showConfirmation(Category category) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_dialog, null);

        Button positiveButton = dialogView.findViewById(R.id.button_positive);
        Button negativeButton = dialogView.findViewById(R.id.button_negative);

        AlertDialog dialog = Alters.showAlertDialog(this, dialogView);

        positiveButton.setOnClickListener(v -> {
                    NavigationUtils.redirectWithPayload(this, UpdateCategory.class, category, "category");
                    dialog.dismiss();
                }
        );


        negativeButton.setOnClickListener(v -> {
            db.delete(category.getId());
            adapter.updateList(db.getAll());
            dialog.dismiss();
            Toaster.showSnackBar(this, findViewById(android.R.id.content), "la categorie a été supprimée avec succès", R.drawable.done, Snackbar.LENGTH_LONG, Snackbar.ANIMATION_MODE_FADE, R.color.white, R.color.black);

        });

        dialog.show();
    }

    private void onAddNewCategory() {
        addNewCategoryBtn.setOnClickListener(v -> {
            Bundle options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_bottom, R.anim.slide_out_top).toBundle();
            NavigationUtils.redirectWithAnimation(this, AddCategoryActivity.class, options);
        });

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.action_bar);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);

            View view = actionBar.getCustomView();
            ImageView profile = view.findViewById(R.id.profile);


            SharedStore store = SharedStore.getOneStore(getApplicationContext());
            var q = new UserQueries(Tables.User, getApplicationContext());

            User u = q.findUserById(store.getInt("userId", -1));

            if (u.getProfilePath() != null) {
                File imgFile = new File(u.getProfilePath());
                if (imgFile.exists()) {
                    profile.setImageURI(Uri.fromFile(imgFile));
                }
            }
            profile.setOnClickListener(v -> {
                NavigationUtils.redirect(this, UserActivity.class);
            });
        }

    }

}