package com.hobbyproject.shoppingapplicationadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityItemDetailsBinding;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityMainBinding;

public class ItemDetailsActivity extends AppCompatActivity {

    private ActivityItemDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}