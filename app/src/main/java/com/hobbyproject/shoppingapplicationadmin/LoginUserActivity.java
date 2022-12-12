package com.hobbyproject.shoppingapplicationadmin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityLoginUserBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginUserActivity extends AppCompatActivity {

    private ActivityLoginUserBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        binding.loginUserCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithEmailPass();
            }
        });

        binding.newUserLoginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUserActivity.this, RegisterUserActivity.class));
            }
        });
    }

    private void loginWithEmailPass() {
        String email = binding.emailAddressLoginId.getText().toString();
        String password = binding.passwordLoginId.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.emailAddressLoginId.setError("Please enter email address.");
            binding.emailAddressLoginId.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordLoginId.setError("Please enter password.");
            binding.passwordLoginId.requestFocus();
        } else if (password.length() != 8 && !isValidPassword(password)) {
            binding.passwordLoginId.setError("Please enter correct password.");
            binding.passwordLoginId.requestFocus();
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginUserActivity.this, MainActivity.class));
                        Toast.makeText(LoginUserActivity.this, "Logged in user successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginUserActivity.this, "Login error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LoginUserActivity.this, MainActivity.class));
        }
    }
}