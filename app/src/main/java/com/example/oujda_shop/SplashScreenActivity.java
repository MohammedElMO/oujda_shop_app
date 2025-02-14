package com.example.oujda_shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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


        new Handler().postDelayed(() -> {

            Intent navigate;

            SharedPreferences store = getSharedPreferences("app-store",MODE_PRIVATE);

            final boolean isLogedIn = store.getBoolean("isLogin",false);

            if(isLogedIn) {
               navigate  = new Intent(this,MainActivity.class);
               startActivity(navigate);

            }else {
                navigate  = new Intent(this,LoginActivity.class);
                startActivity(navigate);
            }


            finish();

        },3000);

    }
}