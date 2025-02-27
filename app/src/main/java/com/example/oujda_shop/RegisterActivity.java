package com.example.oujda_shop;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.oujda_shop.utils.Toaster;

public class RegisterActivity extends AppCompatActivity {

    ImageView visibility_icon;
    EditText passwordField;
    EditText emailField;
    EditText confirmPasswordField;
    EditText firstNameField;
    EditText lastNameField;
    Button registerBtn;
    Button loginBtn;
    UserQueries db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        visibility_icon = findViewById(R.id.pass_icon);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_pass);
        firstNameField = findViewById(R.id.firstName);
        lastNameField = findViewById(R.id.lastName);
        emailField = findViewById(R.id.email);
        registerBtn = findViewById(R.id.register_btn);
        InputUtils.switchVisibility(visibility_icon, getApplicationContext(), passwordField);


        db = new UserQueries(Tables.User, getApplicationContext());

        handleRegister();
    }

    private boolean validateField() {
        boolean isEmptyFirstName = TextUtils.isEmpty(InputUtils.getFieldValue(firstNameField).trim());
        boolean isEmptyLastName = TextUtils.isEmpty(InputUtils.getFieldValue(lastNameField).trim());
        boolean isEmptyEmail = TextUtils.isEmpty(InputUtils.getFieldValue(emailField).trim());
        boolean isEmptyPassword = TextUtils.isEmpty(InputUtils.getFieldValue(passwordField).trim());

        if (isEmptyFirstName || isEmptyLastName || isEmptyEmail || isEmptyPassword) {
            Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "the fields can't be empty", R.drawable.info_icon);
            return false;
        }
        return true;


    }


    private void handleRegister() {

        registerBtn.setOnClickListener(v -> {

            if (!validateField()) {
                return;
            }

            if (!isConfirmed()) {

                Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "the password and confirm password are not the same", R.drawable.info_icon);
                return;
            }


            if (isSameEmail(db)) {
                Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "This email is already registered. Try logging in instead", R.drawable.info_icon);
                return;
            }
            //TODO: make the validation for the fields before inserting the user to the database

            try {
                User createdUser = new User(InputUtils.getFieldValue(firstNameField), InputUtils.getFieldValue(lastNameField), InputUtils.getFieldValue(emailField), InputUtils.getFieldValue(passwordField));

                db.insert(createdUser);
                NavigationUtils.redirect(this, MainActivity.class);

            } catch (Exception e) {
                Log.d("Error", e.getMessage());
                Toaster.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "Something went wrong", R.drawable.error_icon);
            }

        });

    }

    private boolean isSameEmail(UserQueries queries) {
        return queries.findByEmail(InputUtils.getFieldValue(emailField));
    }

    private boolean isConfirmed() {
        return passwordField.getText().toString().equals(confirmPasswordField.getText().toString());

    }
}