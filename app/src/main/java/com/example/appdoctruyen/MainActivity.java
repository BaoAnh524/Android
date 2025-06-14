package com.example.appdoctruyen;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private List<Story> storyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.storyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storyList = new ArrayList<>();
        // Thêm imageResId (sử dụng ic_launcher_background làm ví dụ)
        storyList.add(new Story("Story 1", "This is the content of story 1.", R.drawable.ic_launcher_background));
        storyList.add(new Story("Story 2", "This is the content of story 2.", R.drawable.ic_launcher_background));

        adapter = new StoryAdapter(this, storyList);
        recyclerView.setAdapter(adapter);
    }
}