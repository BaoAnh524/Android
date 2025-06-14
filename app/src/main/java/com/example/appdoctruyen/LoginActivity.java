package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private SharedPreferences sharedPreferences;
    private List<Account> accountList; // Danh sách tài khoản

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Khởi tạo danh sách tài khoản (giả lập)
        accountList = new ArrayList<>();
        accountList.add(new Account("user", "pass"));
        accountList.add(new Account("admin", "admin123"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Kiểm tra đầu vào
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tìm tài khoản trong danh sách
                Account foundAccount = findAccountByUsername(username);
                if (foundAccount != null) {
                    // So sánh mật khẩu
                    if (foundAccount.getPassword().equals(password)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", username); // Lưu username
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish(); // Đóng LoginActivity
                    } else {
                        Toast.makeText(LoginActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Phương thức tìm tài khoản theo username
    private Account findAccountByUsername(String username) {
        for (Account account : accountList) {
            if (account.getUsername().equals(username)) {
                return account;
            }
        }
        return null;
    }
}