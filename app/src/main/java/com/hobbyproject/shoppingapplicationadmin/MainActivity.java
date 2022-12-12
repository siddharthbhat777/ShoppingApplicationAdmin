package com.hobbyproject.shoppingapplicationadmin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ArrayList<DataModalClass> itemsData;
    ItemsDataAdapterClass adapter;
    Uri uri;
    FirebaseAuth auth;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ImageView logout = findViewById(R.id.logoutButton);

        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginUserActivity.class));
                Toast.makeText(MainActivity.this, "User logged out successfully!", Toast.LENGTH_SHORT).show();
            }
        });

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

        binding.firebaseUploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select your image!"), 101);
            }
        });

        binding.addItemLayoutButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToDatabase(uri);
            }
        });
    }

    public void addDataToDatabase(Uri uri) {
        if (uri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference().child( System.currentTimeMillis() + binding.itemTitleEditText.getText().toString() + ".jpg");
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String file_url = task.getResult().toString();


                            Map<String, Object> item = new HashMap<>();
                            item.put("image", file_url);
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
                                            finish();
                                            MainActivity.this.overridePendingTransition(R.anim.nothing, R.anim.nothing);
                                            startActivity(getIntent());

                                        }
                                    });
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Picasso.get().load(uri).into(binding.firebaseUploadImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}