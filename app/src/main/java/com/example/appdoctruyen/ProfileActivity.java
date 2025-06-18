package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileText;
    private Button accountInfoButton, changePasswordButton, changeEmailButton, logoutButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        profileText = findViewById(R.id.profileText);
        accountInfoButton = findViewById(R.id.accountInfoButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        changeEmailButton = findViewById(R.id.changeEmailButton);
        logoutButton = findViewById(R.id.logoutButton);
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
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.Home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.Setting) {
                startActivity(new Intent(ProfileActivity.this, SettingActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.navigation_account) {
                return true; // Ở lại trang hiện tại
            }
            return false;
        });
    }
}