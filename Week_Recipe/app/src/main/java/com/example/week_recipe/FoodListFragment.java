package com.example.week_recipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.week_recipe.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;

public class FoodListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        FoodList foodList = new FoodList();
        foodList.add(new Food("鸡汤", FoodType.Meat,new IngredientsList(),R.drawable.jitang_picture));
        foodList.add(new Food("西红柿蛋汤", FoodType.Vegetarian,new IngredientsList()));
        foodList.add(new Food("红烧牛肉", FoodType.Meat,new IngredientsList(),R.drawable.ic_action_settings));
        foodList.add(new Food("炒青菜", FoodType.Vegetarian,new IngredientsList(),R.drawable.user_picture));
        foodList.add(new Food("水煮白菜", FoodType.Vegetarian,new IngredientsList(),R.drawable.ic_action_home));
        foodList.add(new Food("麻婆豆腐", FoodType.Vegetarian,new IngredientsList(),R.drawable.ic_action_user_info));
        bind(view,foodList);
        return view;
    }

    public static void bind(View view,FoodList foodList)
    {
        RecyclerView foodListView;
        FoodListAdapter foodListAdapter;
        foodListView = view.findViewById(R.id.fragment_todayRecipe_recyclerView);
        foodListView.hasFixedSize();
        foodListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        foodListAdapter = new FoodListAdapter(foodList);
        foodListView.setAdapter(foodListAdapter);

    }
}