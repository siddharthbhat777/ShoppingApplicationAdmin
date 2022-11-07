package com.hobbyproject.shoppingapplicationadmin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private ActivityMainBinding binding;
    ArrayList<DataModalClass> itemsData;
    ItemsDataAdapterClass adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();

        binding.addItemLayout.setVisibility(View.INVISIBLE);

        binding.itemsDisplayRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        itemsData = new ArrayList<>();
        adapter = new ItemsDataAdapterClass(itemsData);
        binding.itemsDisplayRecyclerView.setAdapter(adapter);
        db.collection("items").orderBy("date", Query.Direction.DESCENDING).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> itemsList = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot documentSnapshot : itemsList) {
                                    DataModalClass dataModalClass = documentSnapshot.toObject(DataModalClass.class);
                                    dataModalClass.setId(documentSnapshot.getId());
                                    itemsData.add(dataModalClass);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        binding.addItemButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addItemLayout.setVisibility(View.VISIBLE);
                binding.addItemButtonCard.setVisibility(View.INVISIBLE);
                Animation scale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.add_item_card_layout_animation);
                binding.addItemLayout.startAnimation(scale);
                binding.addItemLayout.startAnimation(scale);
                binding.addItemBg.setBackgroundColor(Color.parseColor("#59000000"));
            }
        });

        binding.cancelLayoutButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addItemLayout.setVisibility(View.INVISIBLE);
                binding.addItemButtonCard.setVisibility(View.VISIBLE);
                Animation scale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.add_item_card_layout_animation_close);
                binding.addItemLayout.startAnimation(scale);
                binding.addItemBg.setBackgroundColor(Color.parseColor("#00000000"));
            }
        });

        binding.addItemLayoutButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToDatabase();
            }
        });
    }

    public void addDataToDatabase() {
        Map<String, Object> item = new HashMap<>();
        item.put("name", binding.itemTitleEditText.getText().toString().trim());
        item.put("brand", binding.itemBrandEditText.getText().toString().trim());
        item.put("price", binding.itemPriceEditText.getText().toString().trim());
        item.put("stock", binding.itemStockEditText.getText().toString().trim());
        item.put("description", binding.itemDescriptionEditText.getText().toString().trim());
        item.put("category", binding.itemCategoryEditText.getText().toString().trim());
        item.put("date", FieldValue.serverTimestamp());

        db.collection("items").add(item)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        binding.addItemLayout.setVisibility(View.INVISIBLE);
                        binding.addItemButtonCard.setVisibility(View.VISIBLE);
                        Animation scale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.add_item_card_layout_animation_close);
                        binding.addItemLayout.startAnimation(scale);
                        binding.addItemBg.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.itemTitleEditText.setText("");
                        binding.itemBrandEditText.setText("");
                        binding.itemPriceEditText.setText("");
                        binding.itemStockEditText.setText("");
                        binding.itemDescriptionEditText.setText("");
                        binding.itemCategoryEditText.setText("");
                        Toast.makeText(MainActivity.this, "Item inserted successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}