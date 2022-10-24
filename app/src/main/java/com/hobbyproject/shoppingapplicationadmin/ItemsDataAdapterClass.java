package com.hobbyproject.shoppingapplicationadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.name.setText(itemsList.get(position).getName());
        holder.brand.setText(itemsList.get(position).getBrand());
        holder.price.setText(itemsList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand, price;
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemTitleTextView);
            brand = itemView.findViewById(R.id.itemBrandTextView);
            price = itemView.findViewById(R.id.itemPriceTextView);
        }
    }
}
