package com.example.week_recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.week_recipe.ViewModel.RecipeWithDateViewModel;
import com.example.week_recipe.adapter.FoodListAdapter;
import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalDate;

public class RecipeWithDateFragment extends Fragment implements FoodListAdapter.OnFoodListItemClickListener {

    private RecipeWithDateViewModel viewModel;
    private TabLayout tabLayout;
    private View fragmentView;
    //private RecyclerView foodListRecyclerView;

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FoodList foodList0 = new FoodList();
        foodList0.add(new Food("鸡汤", FoodType.Meat,new IngredientsList(),R.drawable.jitang_picture));
        foodList0.add(new Food("西红柿蛋汤", FoodType.Vegetarian,new IngredientsList()));
        FoodList foodList1 = new FoodList();
        foodList1.add(new Food("红烧牛肉", FoodType.Meat,new IngredientsList(),R.drawable.ic_action_settings));
        foodList1.add(new Food("炒青菜", FoodType.Vegetarian,new IngredientsList(),R.drawable.user_picture));
        FoodList foodList2 = new FoodList();
        foodList2.add(new Food("水煮白菜", FoodType.Vegetarian,new IngredientsList(),R.drawable.ic_action_home));
        foodList2.add(new Food("麻婆豆腐", FoodType.Other,new IngredientsList(),R.drawable.ic_action_user_info));
        DailyRecipe dailyRecipe = new DailyRecipe(LocalDate.now(),foodList0,foodList1,foodList2);
        SystemModelManager.getSystemModelManager().addDailyRecipe(dailyRecipe);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_with_date, container, false);
        fragmentView = view.findViewById(R.id.fragment_recipeWithDate_fragment);
        tabLayout = view.findViewById(R.id.fragment_recipeWithDate_tabLayout);

        tabLayout.selectTab(tabLayout.getTabAt(1));
        viewModel = new ViewModelProvider(this).get(RecipeWithDateViewModel.class);
        bind();
        return view;
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void bind()
    {
        DailyRecipeFragment fragment = FragmentManager.findFragment(fragmentView);

        viewModel.getShowDateText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tabLayout.getTabAt(1).setText(s);
            }
        });
        viewModel.getShowRecipe().observe(this, new Observer<DailyRecipe>() {
            @Override
            public void onChanged(DailyRecipe dailyRecipe) {
                fragment.bind(dailyRecipe,RecipeWithDateFragment.this,viewModel.getFavouriteFoodList());
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabLayout.getSelectedTabPosition())
                {
                    case 0:
                        viewModel.setShowDate(viewModel.getShowDate().minusDays(1));
                        break;
                    case 1:
                        break;
                    case 2:
                        viewModel.setShowDate(viewModel.getShowDate().plusDays(1));
                        break;
                }
                tabLayout.selectTab(tabLayout.getTabAt(1));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFoodImageClick(int clickedItemIndex) {
        DailyRecipeFragment fragment = FragmentManager.findFragment(fragmentView);
        Food showFood = null;
        DailyRecipe dailyRecipe = viewModel.getShowRecipe().getValue();
        switch (fragment.getTabPosition())
        {
            case 0:
                showFood = dailyRecipe.getBreakfast().getByIndex(clickedItemIndex);
                break;
            case 1:
                showFood = dailyRecipe.getLunch().getByIndex(clickedItemIndex);
                break;
            case 2:
                showFood = dailyRecipe.getDinner().getByIndex(clickedItemIndex);
                break;
        }
        Context context = getContext();
        Intent intent = new Intent(context,FoodInformationActivity.class);
        intent.putExtra("showFood", new Gson().toJson(showFood));
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int clickedItemIndex) {
        DailyRecipeFragment fragment = FragmentManager.findFragment(fragmentView);
        viewModel.deleteFood(fragment.getTabPosition(),clickedItemIndex);
    }

    @Override
    public void onLikeClick(int clickedItemIndex) {
        DailyRecipeFragment fragment = FragmentManager.findFragment(fragmentView);
        viewModel.changeLikeState(fragment.getTabPosition(),clickedItemIndex);
    }
}