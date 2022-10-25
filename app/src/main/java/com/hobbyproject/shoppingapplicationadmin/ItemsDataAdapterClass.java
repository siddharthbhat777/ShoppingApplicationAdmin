package com.hobbyproject.shoppingapplicationadmin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemsDataAdapterClass extends RecyclerView.Adapter<ItemsDataAdapterClass.ItemsViewHolder> {

    ArrayList<DataModalClass> itemsList;

    public ItemsDataAdapterClass(ArrayList<DataModalClass> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        try {
            Picasso.get().load(itemsList.get(position).getImage()).into(holder.itemImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.name.setText(itemsList.get(position).getName());
        holder.brand.setText(itemsList.get(position).getBrand());
        holder.price.setText(itemsList.get(position).getPrice());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.view.getContext(), ItemDetailsActivity.class);
                intent.putExtra("image", itemsList.get(position).getImage());
                intent.putExtra("name", itemsList.get(position).getName());
                intent.putExtra("brand", itemsList.get(position).getBrand());
                intent.putExtra("price", itemsList.get(position).getPrice());
                intent.putExtra("stock", itemsList.get(position).getStock());
                intent.putExtra("description", itemsList.get(position).getDescription());
                intent.putExtra("category", itemsList.get(position).getCategory());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.view.getContext().startActivity(intent);
            }
        });
        /*if (!itemsList.get(position).getStock().equals("0")) {
            holder.outOfStock.setVisibility(View.INVISIBLE);
        } else {
            holder.outOfStock.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand, price;
        LinearLayout outOfStock;
        ImageView itemImage;
        View view;
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemTitleTextView);
            brand = itemView.findViewById(R.id.itemBrandTextView);
            price = itemView.findViewById(R.id.itemPriceTextView);
            outOfStock = itemView.findViewById(R.id.outOfStockLayout);
            itemImage = itemView.findViewById(R.id.itemImageView);
            this.view = itemView;
        }
    }
}
