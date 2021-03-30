package com.example.week_recipe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private final FoodList foodList;
    private final OnFoodListItemClickListener onFoodListItemClickListener;
    private ArrayList<View> itemViewList;

    public FoodListAdapter(FoodList foodList,OnFoodListItemClickListener listener)
    {
        itemViewList = new ArrayList<>();
        if (foodList==null)
        {
            this.foodList = new FoodList();
        }
        else
        {
            this.foodList = foodList;
        }
        onFoodListItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_list_item,parent,false);
        itemViewList.add(view);
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


    public View getItemView(int position)
    {
        return itemViewList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView foodName;
        ImageView foodImage;
        ImageView more;
        ImageView delete;
        CardView cardView;

        ViewHolder(View itemView)
        {
            super(itemView);
            foodName = itemView.findViewById(R.id.item_recipeList_foodName);
            foodImage = itemView.findViewById(R.id.item_recipeList_foodImage);
            more = itemView.findViewById(R.id.item_recipeList_more);
            delete = itemView.findViewById(R.id.item_recipeList_delete);
            cardView = itemView.findViewById(R.id.item_recipeList_cardView);
            delete.setVisibility(View.GONE);

            foodImage.setOnClickListener(this);
            more.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(foodImage))
            {
                onFoodListItemClickListener.onFoodImageClick(getAdapterPosition());
            }
            else if (v.equals(more))
            {
                switch (delete.getVisibility()) {
                    case View.GONE:
                        delete.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        delete.setVisibility(View.GONE);
                        break;
                }
                for (int x=0;x<itemViewList.size();x++)
                {
                    if (x!=getAdapterPosition())
                    {
                        itemViewList.get(x).findViewById(R.id.item_recipeList_delete).setVisibility(View.GONE);
                    }
                }
            }
            else if (v.equals(delete))
            {
                onFoodListItemClickListener.onDeleteClick(getAdapterPosition());
            }
        }
    }

    public interface OnFoodListItemClickListener {
        void onFoodImageClick(int clickedItemIndex);
        void onDeleteClick(int clickedItemIndex);
    }
}
