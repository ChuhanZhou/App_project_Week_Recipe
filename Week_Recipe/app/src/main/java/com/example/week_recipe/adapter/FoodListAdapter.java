package com.example.week_recipe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private FoodList foodList;

    public FoodListAdapter(FoodList foodList)
    {
        this.foodList = foodList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.foodName.setText(foodList.getByIndex(position).getName());
        holder.foodImage.setImageResource(foodList.getByIndex(position).getImageId());
    }

    @Override
    public int getItemCount() {
        return foodList.getSize();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView foodName;
        ImageView foodImage;

        ViewHolder(View itemView)
        {
            super(itemView);
            foodName = itemView.findViewById(R.id.item_recipeList_foodName);
            foodImage = itemView.findViewById(R.id.item_recipeList_foodImage);
        }
    }
}
