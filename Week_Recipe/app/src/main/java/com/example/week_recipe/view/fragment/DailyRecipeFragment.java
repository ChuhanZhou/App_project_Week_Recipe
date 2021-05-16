package com.example.week_recipe.view.fragment;

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

import com.example.week_recipe.R;
import com.example.week_recipe.view.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class DailyRecipeFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private View view;
    FoodListFragment fragment;
    private TabLayout tabLayout;
    private DailyRecipe dailyRecipe;
    private FloatingActionButton addFoodButton;
    private int tabPosition = 0;
    private FoodListAdapter.OnFoodListItemClickListener onFoodListItemClickListener;
    private  LiveData<FoodList> favouriteFoodList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_daily_recipe, container, false);
        return view;
    }

    public void bind(DailyRecipe dailyRecipe, int tabPosition, FoodListAdapter.OnFoodListItemClickListener listener, LiveData<FoodList> favouriteFoodList)
    {
        onFoodListItemClickListener = listener;
        if (tabPosition>=0&&tabPosition<3)
        {
            this.tabPosition = tabPosition;
        }
        this.favouriteFoodList = favouriteFoodList;
        fragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_dailyRecipe_fragment));
        tabLayout = view.findViewById(R.id.fragment_dailyRecipe_tabLayout);
        addFoodButton = view.findViewById(R.id.fragment_dailyRecipe_addFoodButton);
        this.dailyRecipe = dailyRecipe;

        tabLayout.selectTab(tabLayout.getTabAt(this.tabPosition));
        updateFragment();
        addListener();
    }

    private void addListener()
    {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tabLayout.getSelectedTabPosition();
                updateFragment();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void updateDailyRecipe(DailyRecipe dailyRecipe,boolean showAnimation)
    {
        this.dailyRecipe = dailyRecipe;
        if (dailyRecipe!=null)
        {
            switch (tabPosition)
            {
                case 0:
                    fragment.updateFoodList(this.dailyRecipe.getBreakfast(),showAnimation);
                    break;
                case 1:
                    fragment.updateFoodList(this.dailyRecipe.getLunch(),showAnimation);
                    break;
                case 2:
                    fragment.updateFoodList(this.dailyRecipe.getDinner(),showAnimation);
                    break;
            }
        }
    }

    private void updateFragment()
    {
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

    public FloatingActionButton getAddFoodButton() {
        return addFoodButton;
    }
}