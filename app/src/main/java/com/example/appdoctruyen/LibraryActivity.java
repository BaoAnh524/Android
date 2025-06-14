package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LibraryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(LibraryActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.navigation_library) {
                return true;
            } else if (id == R.id.navigation_account) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                if (isLoggedIn) {
                    startActivity(new Intent(LibraryActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(LibraryActivity.this, LoginActivity.class));
                }
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}