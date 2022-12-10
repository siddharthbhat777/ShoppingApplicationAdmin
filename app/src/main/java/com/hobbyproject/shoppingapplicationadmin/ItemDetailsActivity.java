package com.hobbyproject.shoppingapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hobbyproject.shoppingapplicationadmin.databinding.ActivityItemDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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

        String itemName = getIntent().getStringExtra("name");
        String itemBrand = getIntent().getStringExtra("brand");
        String itemPrice = getIntent().getStringExtra("price");
        String itemStock = getIntent().getStringExtra("stock");
        String itemDescription = getIntent().getStringExtra("description");
        String itemCategory = getIntent().getStringExtra("category");

        Picasso.get().load(getIntent().getStringExtra("image")).into(binding.itemImageDetailImageView);
        binding.itemTitleDetailTextView.setText(itemName);
        binding.itemBrandDetailTextView.setText(itemBrand);
        binding.itemPriceDetailTextView.setText("₹" + itemPrice + "/-");
        if (itemStock.equals("0")) {
            binding.itemStockDetailTextView.setText("Out of Stock");
            binding.itemStockDetailTextView.setTextColor(Color.parseColor("#FF0000"));
        } else {
            binding.itemStockDetailTextView.setText("In stock (" + itemStock + ")");
        }
        binding.itemDescriptionDetailTextView.setText(itemDescription);
        binding.itemCategoryDetailTextView.setText("Category: " + itemCategory);

        binding.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDialog dd = new DeleteDialog();
                dd.showDialog(ItemDetailsActivity.this);
            }
        });

        binding.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateDialog ud = new UpdateDialog();
                ud.showDialog(ItemDetailsActivity.this, itemName, itemBrand, itemPrice,itemStock,itemDescription, itemCategory);
            }
        });
    }

    public class DeleteDialog {
        public void showDialog(ItemDetailsActivity itemDetailsActivity) {
            final Dialog dialog = new Dialog(itemDetailsActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.delete_dialog_box);
            dialog.show();
            LinearLayout yes, no;
            yes = (LinearLayout) dialog.findViewById(R.id.deleteSubmitLayout);
            no = (LinearLayout) dialog.findViewById(R.id.cancelDeleteLayout);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = getIntent().getStringExtra("position");
                    db.collection("items").document(id).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ItemDetailsActivity.this, "Your item has been deleted!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    public class UpdateDialog {
        public void showDialog(ItemDetailsActivity itemDetailsActivity, String itemName, String itemBrand, String itemPrice, String itemStock, String itemDescription, String itemCategory) {
            final Dialog dialog = new Dialog(itemDetailsActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.update_dialog_box);
            dialog.show();
            LinearLayout yes, no;
            EditText name, brand, price, stock, description, category;
            yes = (LinearLayout) dialog.findViewById(R.id.editSubmitLayout);
            no = (LinearLayout) dialog.findViewById(R.id.cancelLayout);
            name = (EditText) dialog.findViewById(R.id.updateNameEditText);
            brand = (EditText) dialog.findViewById(R.id.updateBrandEditText);
            price = (EditText) dialog.findViewById(R.id.updatePriceEditText);
            stock = (EditText) dialog.findViewById(R.id.updateStockEditText);
            description = (EditText) dialog.findViewById(R.id.updateDescriptionEditText);
            category = (EditText) dialog.findViewById(R.id.updateCategoryEditText);

            name.setText(itemName);
            brand.setText(itemBrand);
            price.setText(itemPrice);
            stock.setText(itemStock);
            description.setText(itemDescription);
            category.setText(itemCategory);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = getIntent().getStringExtra("position");
                    db.collection("items").document(id).update(
                                    "name", name.getText().toString().trim(),
                                    "brand", brand.getText().toString().trim(),
                                    "price", price.getText().toString().trim(),
                                    "stock", stock.getText().toString().trim(),
                                    "description", description.getText().toString().trim(),
                                    "category", category.getText().toString().trim()
                            )
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ItemDetailsActivity.this, "Your item has been updated!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }
}