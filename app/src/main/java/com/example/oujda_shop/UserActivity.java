package com.example.oujda_shop;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.DAOs.UserQueries;
import com.example.oujda_shop.entities.Tables;
import com.example.oujda_shop.entities.User;
import com.example.oujda_shop.utils.ImageUtils;
import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.SharedStore;

import java.io.File;

public class UserActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView profileImage;
    private EditText userName, userEmail, new_password;
    private UserQueries db;

    private Uri imageUri;
    SharedStore store;
    private Button changeProfileImageBtn, changePasswordBtn, saveChangesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpActionBar();
        store = SharedStore.getOneStore(getApplicationContext());

        if(!store.getBoolean("isLogin",false)) {
            NavigationUtils.redirect(this, LoginActivity.class);
        }
        profileImage = findViewById(R.id.profile_image);
        changeProfileImageBtn = findViewById(R.id.change_profile_image_btn);
        changePasswordBtn = findViewById(R.id.change_password_btn);
        saveChangesBtn = findViewById(R.id.save_changes_btn);
        userName = findViewById(R.id.user_name);
        new_password = findViewById(R.id.new_password);
        userEmail = findViewById(R.id.user_email);
        db = new UserQueries(Tables.User, getApplicationContext());


        User u = db.loadUserData(store.getInt("userId", -1));

        userName.setText(u.getFirstName() + " " + u.getLastName());
        userEmail.setText(u.getEmail());

        if (u.getProfilePath() != null) {
            File imgFile = new File(u.getProfilePath());
            if (imgFile.exists()) {
                profileImage.setImageURI(Uri.fromFile(imgFile));
            }
        }

        changeProfileImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        changePasswordBtn.setOnClickListener(v -> {
            updatePassword();
        });

        saveChangesBtn.setOnClickListener(v -> updateUserInfo());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);

            // Save image path in database
            String imagePath = ImageUtils.saveImageToFile(imageUri, getApplicationContext());
            db.updateProfileImage(store.getInt("userId", -1), imagePath);
        }
    }

    private void updatePassword() {
        String newPassword = new_password.getText().toString();

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.updateUserPassword(store.getInt("userId", -1), newPassword);

        Toast.makeText(this, "Password updated successfully. Please log in again.", Toast.LENGTH_LONG).show();

       NavigationUtils.redirect(this,LoginActivity.class);
       store.clear();
    }

    private void updateUserInfo() {
        String newName = userName.getText().toString().trim();
        String newEmail = userEmail.getText().toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "name and email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        db.updateUserDetails(store.getInt("userId", -1), newName, newEmail);
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
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
            actionBar.setCustomView(R.layout.action_bar_profiler);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }


}