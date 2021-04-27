package com.example.week_recipe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Ingredients;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.view.fragment.HorizontalIngredientsListFragment;

import java.util.ArrayList;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder>{
    private IngredientsList ingredientsList;
    //private ArrayList<IngredientsListAdapter.ViewHolder> viewHolderList;
    private ArrayList<HorizontalIngredientsListFragment> lineList;
    private final HorizontalIngredientsListAdapter.OnItemClickListener clickListener;
    private final boolean needSet;
    //private boolean afterBind;
    private boolean countLock;
    private int itemCount;

    public IngredientsListAdapter(IngredientsList ingredientsList,boolean needSet,HorizontalIngredientsListAdapter.OnItemClickListener clickListener)
    {
        updateIngredientsList(ingredientsList);
        this.needSet = needSet;
        this.clickListener= clickListener;
    }

    public void updateIngredientsList(IngredientsList ingredientsList)
    {
        if (ingredientsList==null)
        {
            this.ingredientsList = new IngredientsList();
            itemCount = 1;
        }
        else
        {
            this.ingredientsList = ingredientsList;
            itemCount = 2;
        }
        //viewHolderList = new ArrayList<>();
        lineList = new ArrayList<>();
        //afterBind = false;
        countLock = false;
    }

    private void updateItem(IngredientsListAdapter.ViewHolder holder, IngredientsList ingredientsList)
    {
        holder.fragment.bind(ingredientsList,needSet,clickListener);
    }

    @NonNull
    @Override
    public IngredientsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_horizontal_ingredients_list, parent, false);
        IngredientsListAdapter.ViewHolder viewHolder = new IngredientsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (lineList.size()<=position&&!countLock)
        {
            if (lineList.size()>0&&lineList.get(lineList.size()-1).getOverflowList().getSize()==0)
            {
                //itemCount = lineList.size()+1;
                countLock = true;
                System.out.println("===============locked");
            }
            else
            {
                itemCount++;
            }
            lineList.add(holder.fragment);
            System.out.println("count:"+itemCount);

            if (position!=0)
            {
                IngredientsList overflowList = lineList.get(position-1).getOverflowList();
                updateItem(holder,overflowList);
            }
            else
            {
                updateItem(holder,ingredientsList);
            }
            System.out.println("holder bind new:"+position);
        }
        else if (lineList.size()>position)
        {
            holder.fragment = lineList.get(position);
            holder.fragment.reShow();
            System.out.println("holder bind old:"+position);
        }
    }

    @Override
    public int getItemCount() {

        //afterBind = false;
        return lineList.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private HorizontalIngredientsListFragment fragment;

        ViewHolder(View itemView) {
            super(itemView);
            fragment = new HorizontalIngredientsListFragment(itemView);
        }

        @Override
        public void onClick(View v) {
            System.out.println("==========");
        }

        public HorizontalIngredientsListFragment getFragment() {
            return fragment;
        }
    }
}