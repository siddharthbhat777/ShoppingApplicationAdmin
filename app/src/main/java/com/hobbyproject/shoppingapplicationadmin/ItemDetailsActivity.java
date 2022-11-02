package com.hobbyproject.shoppingapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityItemDetailsBinding;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class ItemDetailsActivity extends AppCompatActivity {

    private ActivityItemDetailsBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("position");

        Picasso.get().load(getIntent().getStringExtra("image")).into(binding.itemImageDetailImageView);
        binding.itemTitleDetailTextView.setText(getIntent().getStringExtra("name"));
        binding.itemBrandDetailTextView.setText(getIntent().getStringExtra("brand"));
        binding.itemPriceDetailTextView.setText("₹" + getIntent().getStringExtra("price") + "/-");
        if (getIntent().getStringExtra("stock").equals("0")) {
            binding.itemStockDetailTextView.setText("Out of Stock");
            binding.itemStockDetailTextView.setTextColor(Color.parseColor("#FF0000"));
        } else {
            binding.itemStockDetailTextView.setText("In stock (" + getIntent().getStringExtra("stock") + ")");
        }
        binding.itemDescriptionDetailTextView.setText(getIntent().getStringExtra("description"));
        binding.itemCategoryDetailTextView.setText("Category: " + getIntent().getStringExtra("category"));

        binding.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("items").document(id).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                            }
                        });
            }
        });
    }
}