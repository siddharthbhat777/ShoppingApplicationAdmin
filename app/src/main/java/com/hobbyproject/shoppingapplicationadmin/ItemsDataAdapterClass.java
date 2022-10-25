package com.hobbyproject.shoppingapplicationadmin;

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
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemTitleTextView);
            brand = itemView.findViewById(R.id.itemBrandTextView);
            price = itemView.findViewById(R.id.itemPriceTextView);
            outOfStock = itemView.findViewById(R.id.outOfStockLayout);
            itemImage = itemView.findViewById(R.id.itemImageView);
        }
    }
}
