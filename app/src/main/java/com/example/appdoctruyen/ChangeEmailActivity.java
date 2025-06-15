package com.example.appdoctruyen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChangeEmailActivity extends AppCompatActivity {
    private EditText currentPasswordEditText, emailEditText;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        currentPasswordEditText = findViewById(R.id.currentPassword);
        emailEditText = findViewById(R.id.email);
        Button saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);
        db = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        Account account = db.getAccountByUsername(username);
        if (account != null) {
            emailEditText.setText(account.getEmail());
        }

        saveButton.setOnClickListener(v -> {
            String currentPassword = currentPasswordEditText.getText().toString().trim();
            String newEmail = emailEditText.getText().toString().trim();
            if (!newEmail.contains("@")) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            String newUsername = newEmail.split("@")[0]; // Trích xuất username mới

            if (currentPassword.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (account != null && account.getPassword().equals(currentPassword)) {
                account.setUsername(newUsername);
                account.setEmail(newEmail);
                if (db.updateAccount(account)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", newUsername);
                    editor.putString("email", newEmail);
                    editor.apply();
                    Toast.makeText(this, "Đổi email thành công", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(this, "Đổi email thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> onBackPressed());
    }
}