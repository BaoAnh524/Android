
package com.example.appdoctruyen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AccountInfoActivity extends AppCompatActivity {
    private EditText usernameEditText, phoneEditText, birthYearEditText, genderEditText;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Account_Info");

        usernameEditText = findViewById(R.id.username);
        phoneEditText = findViewById(R.id.phone);
        birthYearEditText = findViewById(R.id.birthYear);
        genderEditText = findViewById(R.id.gender);
        Button saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);
        db = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Load thông tin hiện tại
        String username = sharedPreferences.getString("username", "");
        Account account = db.getAccountByUsername(username);
        if (account != null) {
            usernameEditText.setText(account.getUsername());
            phoneEditText.setText(account.getPhone());
            birthYearEditText.setText(account.getBirthYear());
            genderEditText.setText(account.getGender());
        }

        saveButton.setOnClickListener(v -> {
            String newUsername = usernameEditText.getText().toString().trim();
            String newPhone = phoneEditText.getText().toString().trim();
            String newBirthYear = birthYearEditText.getText().toString().trim();
            String newGender = genderEditText.getText().toString().trim();

            Account updatedAccount = new Account(newUsername, account != null ? account.getPassword() : "", newPhone, newBirthYear, newGender, account != null ? account.getEmail() : "");
            if (db.updateAccount(updatedAccount)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", newUsername);
                editor.putString("phone", newPhone);
                editor.putString("birthYear", newBirthYear);
                editor.putString("gender", newGender);
                editor.apply();
                Toast.makeText(this, "Thông tin đã được lưu", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lưu thông tin thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> onBackPressed());
    }
}
