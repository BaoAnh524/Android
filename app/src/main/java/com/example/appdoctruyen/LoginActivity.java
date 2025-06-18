package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailEditText, passwordEditText;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Login");

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        SignInButton googleSignInButton = findViewById(R.id.googleSignInButton);
        Button forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        db = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();

        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, gso);
        } catch (Exception e) {
            Log.e(TAG, "GoogleSignInOptions error: " + e.getMessage());
            Toast.makeText(this, "Lỗi cấu hình Google Sign-In", Toast.LENGTH_SHORT).show();
        }

        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(TAG, "Google Sign-In success: " + account.getEmail());
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            Log.w(TAG, "Google sign in failed: " + e.getStatusCode() + " - " + e.getMessage());
                            Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Sign-in result not OK: " + result.getResultCode());
                    }
                }
        );

        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
        forgotPasswordButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (!email.contains("@")) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            String username = email.split("@")[0];
            String password = passwordEditText.getText().toString().trim();

            Account account = db.getAccountByUsername(username);
            if (account != null && account.getPassword().equals(password)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("username", username);
                editor.putString("phone", account.getPhone());
                editor.putString("birthYear", account.getBirthYear());
                editor.putString("gender", account.getGender());
                editor.putString("email", account.getEmail());
                editor.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void signInWithGoogle() {
        if (googleSignInClient != null) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        } else {
            Log.e(TAG, "googleSignInClient is null");
            Toast.makeText(this, "Lỗi khởi tạo Google Sign-In", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        if (idToken != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String email = user.getEmail();
                                String username = email != null ? email.split("@")[0] : "unknown";
                                Account account = db.getAccountByUsername(username);
                                if (account == null) {
                                    account = new Account(username, "", "", "", "", email);
                                    try {
                                        db.addAccount(account);
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error adding account to database: " + e.getMessage());
                                        Toast.makeText(this, "Lỗi lưu tài khoản", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.apply();
                                Log.d(TAG, "Firebase auth successful, redirecting to HomeActivity");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Log.w(TAG, "FirebaseUser is null after successful sign-in");
                                Toast.makeText(this, "Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(this, "Xác thực Firebase thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.e(TAG, "idToken is null");
            Toast.makeText(this, "Lỗi token Google Sign-In", Toast.LENGTH_SHORT).show();
        }
    }
}