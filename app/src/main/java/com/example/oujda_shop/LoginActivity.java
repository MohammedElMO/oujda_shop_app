package com.example.oujda_shop;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.DAOs.UserQueries;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.entities.User;
import com.example.oujda_shop.utils.InputUtils;
import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.SharedStore;
import com.example.oujda_shop.utils.Toaster;
import com.google.android.material.snackbar.Snackbar;


public class LoginActivity extends AppCompatActivity {
    ImageView visibility_icon;
    EditText passwordField;
    EditText emailField;
    Button createAccountBtn;
    Button loginBtn;
    UserQueries db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        visibility_icon = findViewById(R.id.pass_icon);
        passwordField = findViewById(R.id.password);
        emailField = findViewById(R.id.email);
        createAccountBtn = findViewById(R.id.sign_up_btn);
        loginBtn = findViewById(R.id.login_btn);
        InputUtils.switchVisibility(visibility_icon, getApplicationContext(), passwordField);

        db = new UserQueries(Tables.User, getApplicationContext());

        handleLogin();
        toCreateAccountActivity();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void toCreateAccountActivity() {
        createAccountBtn.setOnClickListener(v -> {
            NavigationUtils.redirect(this, RegisterActivity.class);
        });
    }

    private String getFieldValue(EditText field) {
        return field.getText().toString();
    }

    private boolean validateField() {
        boolean isEmptyEmail = TextUtils.isEmpty(InputUtils.getFieldValue(emailField).trim());
        boolean isEmptyPassword = TextUtils.isEmpty(InputUtils.getFieldValue(passwordField).trim());

        if (isEmptyEmail || isEmptyPassword) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "the fields can't be empty", R.drawable.info_icon);
            return false;
        }
        return true;


    }

    private void handleLogin() {

        SharedStore store = SharedStore.getOneStore(getApplicationContext());


        loginBtn.setOnClickListener(v -> {
            if (!validateField()) {
                return;
            }
            boolean isCorrectCredentials = db.login(getFieldValue(emailField), getFieldValue(passwordField));
            int userId  = db.findUserByEmail(emailField.getText().toString());

            if (isCorrectCredentials) {
                store.saveBoolean("isLogin", true);
                store.saveInt("userId", userId);
                NavigationUtils.redirect(this, MainActivity.class);
                Snackbar.make(findViewById(android.R.id.content), "Nice Your are login", Snackbar.LENGTH_SHORT).show();
            } else {
                Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "the provided credentials are not correct or you are not registered", R.drawable.info_icon);
            }

        });

    }


}