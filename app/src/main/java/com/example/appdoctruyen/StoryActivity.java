package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        TextView contentTextView = findViewById(R.id.storyContent);
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        setTitle(title);
        contentTextView.setText(content);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    startActivity(new Intent(StoryActivity.this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.navigation_library) {
                    startActivity(new Intent(StoryActivity.this, LibraryActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.navigation_account) {
                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                    if (isLoggedIn) {
                        startActivity(new Intent(StoryActivity.this, ProfileActivity.class));
                    } else {
                        startActivity(new Intent(StoryActivity.this, LoginActivity.class));
                    }
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}