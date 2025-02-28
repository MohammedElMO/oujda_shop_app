package com.example.oujda_shop;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oujda_shop.utils.NavigationUtils;
import com.example.oujda_shop.utils.SharedStore;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final int TIMER = 300;
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(() -> {
            SharedStore store = SharedStore.getOneStore(getApplicationContext());

            final boolean isLogin = store.getBoolean("isLogin", false);

            if (!isLogin) {
                NavigationUtils.redirect(this, LoginActivity.class);
            } else {
                NavigationUtils.redirect(this, MainActivity.class);

            }


            finish();

        }, TIMER);

    }
}