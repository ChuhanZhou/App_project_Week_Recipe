package com.example.week_recipe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Ingredients;
import com.example.week_recipe.model.domain.food.IngredientsList;

import java.util.ArrayList;

public class HorizontalIngredientsListAdapter extends RecyclerView.Adapter<HorizontalIngredientsListAdapter.ViewHolder> {
    private IngredientsList ingredientsList;
    private ArrayList<HorizontalIngredientsListAdapter.ViewHolder> viewHolderList;
    private final boolean needSet;
    private final HorizontalIngredientsListAdapter.OnItemClickListener clickListener;
    private boolean checkLast;

    public HorizontalIngredientsListAdapter(IngredientsList ingredientsList,boolean needSet,HorizontalIngredientsListAdapter.OnItemClickListener clickListener)
    {
        updateIngredientsList(ingredientsList,true);
        this.needSet = needSet;
        this.clickListener= clickListener;
        viewHolderList = new ArrayList<>();
    }

    public void updateIngredientsList(IngredientsList ingredientsList,boolean checkLast)
    {
        if (ingredientsList==null)
        {
            this.ingredientsList = new IngredientsList();
        }
        else
        {
            this.ingredientsList = ingredientsList;
        }
        viewHolderList = new ArrayList<>();
        this.checkLast = checkLast;
    }

    private void updateItem(ViewHolder holder, Ingredients ingredients)
    {
        if (ingredients!=null)
        {
            holder.name.setText(ingredients.getName());
        }
        //else if (checkLast)
        //{
        //    if (ingredientsList!=null&&ingredientsList.getSize()>0)
        //    {
        //        holder.name.setText(ingredientsList.getByIndex(ingredientsList.getSize()-1).getName());
        //    }
        //}
    }

    @NonNull
    @Override
    public HorizontalIngredientsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ingredients_list, parent, false);
        HorizontalIngredientsListAdapter.ViewHolder viewHolder = new HorizontalIngredientsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalIngredientsListAdapter.ViewHolder holder, int position) {
        viewHolderList.add(holder);
        updateItem(holder,ingredientsList.getByIndex(position));
        if (ingredientsList.getByIndex(position)!=null&&!checkLast)
        {
            System.out.println("bind:"+ingredientsList.getByIndex(position).getName());
        }

    }

    @Override
    public int getItemCount() {
        if (checkLast)
        {
            return ingredientsList.getSize()+1;
        }
        else
        {
            return ingredientsList.getSize();
        }
    }

    public int getSizeOfViewHolderList()
    {
        return viewHolderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final ImageView delete;
        private final View view;
        private final CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.item_ingredientsList_name);
            delete = itemView.findViewById(R.id.item_ingredientsList_delete);
            cardView = itemView.findViewById(R.id.item_ingredientsList_cardView);
            if (needSet)
            {
                delete.setVisibility(View.VISIBLE);
            }
            else
            {
                delete.setVisibility(View.INVISIBLE);
            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("------------------");
                    clickListener.onItemClick(ingredientsList.getByIndex(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {
        }

        public View getView() {
            return view;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Ingredients ingredients);
    }
}
