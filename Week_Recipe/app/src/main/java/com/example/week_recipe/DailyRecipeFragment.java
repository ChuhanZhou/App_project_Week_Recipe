package com.example.week_recipe;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.week_recipe.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;

public class DailyRecipeFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private View view;
    private View fragmentView;
    private TabLayout tabLayout;
    private DailyRecipe dailyRecipe;
    private int tabPosition = 0;
    private FoodListAdapter.OnFoodListItemClickListener onFoodListItemClickListener;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_daily_recipe, container, false);
        return view;
    }

    public void bind(DailyRecipe dailyRecipe, FoodListAdapter.OnFoodListItemClickListener listener, LiveData<FoodList> favouriteFoodList)
    {
        onFoodListItemClickListener = listener;
        fragmentView = view.findViewById(R.id.fragment_dailyRecipe_fragment);
        tabLayout = view.findViewById(R.id.fragment_dailyRecipe_tabLayout);
        this.dailyRecipe = dailyRecipe;

        tabLayout.selectTab(tabLayout.getTabAt(tabPosition));
        updateFragment(favouriteFoodList);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tabLayout.getSelectedTabPosition();
                updateFragment(favouriteFoodList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void updateFragment(LiveData<FoodList> favouriteFoodList)
    {
        FoodListFragment fragment = FragmentManager.findFragment(fragmentView);
        if (dailyRecipe!=null)
        {
            switch (tabPosition)
            {
                case 0:
                    fragment.bind(dailyRecipe.getBreakfast(),onFoodListItemClickListener,true,true,true,favouriteFoodList);
                    break;
                case 1:
                    fragment.bind(dailyRecipe.getLunch(),onFoodListItemClickListener,true,true,true,favouriteFoodList);
                    break;
                case 2:
                    fragment.bind(dailyRecipe.getDinner(),onFoodListItemClickListener,true,true,true,favouriteFoodList);
                    break;
            }
        }
        else
        {
            fragment.bind(null,onFoodListItemClickListener,true,true,true,favouriteFoodList);
        }
    }

    public int getTabPosition() {
        return tabPosition;
    }
}