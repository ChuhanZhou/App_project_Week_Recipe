package com.example.week_recipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.week_recipe.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;

public class FoodListFragment extends Fragment implements FoodListAdapter.OnFoodListItemClickListener{

    private View view;
    private TextView noDataTextView;
    private FoodList foodList;
    private FoodListAdapter foodListAdapter;
    private RecyclerView foodListView;
    private FoodListAdapter.OnFoodListItemClickListener onFoodListItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food_list, container, false);
        return view;
    }

    public void bind(FoodList foodList, FoodListAdapter.OnFoodListItemClickListener listener)
    {
        this.foodList = foodList;
        onFoodListItemClickListener = listener;
        foodListView = view.findViewById(R.id.fragment_foodList_recyclerView);
        foodListAdapter = new FoodListAdapter(this.foodList,this);
        noDataTextView = view.findViewById(R.id.fragment_foodList_noDataTextView);

        setNoDataTextViewVisibility();

        foodListView.hasFixedSize();
        foodListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        foodListView.setAdapter(foodListAdapter);
    }

    private void setNoDataTextViewVisibility()
    {
        if (foodList==null||foodList.getSize()==0)
        {
            noDataTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noDataTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFoodImageClick(int clickedItemIndex) {
        onFoodListItemClickListener.onFoodImageClick(clickedItemIndex);
    }

    @Override
    public void onDeleteClick(int clickedItemIndex) {
        onFoodListItemClickListener.onDeleteClick(clickedItemIndex);
    }
}