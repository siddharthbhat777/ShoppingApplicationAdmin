package com.hobbyproject.shoppingapplicationadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<String> categories;
    int selectedPosition = 0;
    CategoryClickInterface itemClick;

    public CategoryAdapter(ArrayList<String> categories, CategoryClickInterface itemClick) {
        this.categories = categories;
        this.itemClick = itemClick;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        holder.textView.setText(categories.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                itemClick.onCategoryClick(position, categories.get(position));
                notifyDataSetChanged();
            }
        });

        if (selectedPosition != position) {
            holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.category_color));
            holder.textView.setTextColor(holder.textView.getContext().getResources().getColor(R.color.white));
        } else {
            holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.logo_bg));
            holder.textView.setTextColor(holder.textView.getContext().getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.categoryView);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
