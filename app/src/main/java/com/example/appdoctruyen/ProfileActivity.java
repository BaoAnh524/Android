package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView profileText = findViewById(R.id.profileText);
        Button accountInfoButton = findViewById(R.id.accountInfoButton);
        Button changePasswordButton = findViewById(R.id.changePasswordButton);
        Button changeEmailButton = findViewById(R.id.changeEmailButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Khách");

        profileText.setText("Hồ sơ của: " + username);

        accountInfoButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, AccountInfoActivity.class)));
        changePasswordButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class)));
        changeEmailButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ChangeEmailActivity.class)));

        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.remove("username");
            editor.remove("phone");
            editor.remove("birthYear");
            editor.remove("gender");
            editor.remove("email");
            editor.apply();
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.navigation_library) {
                startActivity(new Intent(ProfileActivity.this, LibraryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.navigation_account) {
                return true;
            }
            return false;
        });
    }
}