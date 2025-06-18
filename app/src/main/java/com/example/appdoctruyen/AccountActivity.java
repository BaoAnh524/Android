package com.example.appdoctruyen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.Home) {
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }  else if (id == R.id.Setting) {
                startActivity(new Intent(AccountActivity.this, SettingActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.navigation_account) {
                return true;
            }
            return false;
        });
    }
}