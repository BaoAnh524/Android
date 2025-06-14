package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private List<Story> storyList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.storyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storyList = new ArrayList<>();
        storyList.add(new Story("Story 1", "Content 1", R.drawable.ic_launcher_background));
        storyList.add(new Story("Story 2", "Content 2", R.drawable.ic_launcher_background));

        adapter = new StoryAdapter(this, storyList);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    return true;
                } else if (id == R.id.navigation_library) {
                    startActivity(new Intent(HomeActivity.this, LibraryActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.navigation_account) {
                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                    if (isLoggedIn) {
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    } else {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}