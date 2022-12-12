package com.hobbyproject.shoppingapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityMainBinding;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityRegisterUserBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUserActivity extends AppCompatActivity {

    private ActivityRegisterUserBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        binding.registerUserCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithEmailPass();
            }
        });

        binding.existingUserRegisterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterUserActivity.this, LoginUserActivity.class));
            }
        });
    }

    private void registerWithEmailPass() {
        String email = binding.emailAddressRegisterId.getText().toString();
        String password = binding.passwordRegisterId.getText().toString();
        String confirmPassword = binding.confirmPasswordRegisterId.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.emailAddressRegisterId.setError("Please enter email address.");
            binding.emailAddressRegisterId.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordRegisterId.setError("Please enter password.");
            binding.passwordRegisterId.requestFocus();
        } else if (password.length() != 8 && !isValidPassword(password)) {
            binding.passwordRegisterId.setError("Please enter correct password.");
            binding.passwordRegisterId.requestFocus();
        } else if (TextUtils.isEmpty(confirmPassword))  {
            binding.confirmPasswordRegisterId.setError("Please confirm your password");
            binding.confirmPasswordRegisterId.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            binding.confirmPasswordRegisterId.setError("Please enter same password in both fields.");
            binding.confirmPasswordRegisterId.requestFocus();
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterUserActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterUserActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Registration error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
}