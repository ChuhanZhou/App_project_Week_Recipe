package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.week_recipe.R;
import com.example.week_recipe.dao.Repository;
import com.example.week_recipe.view.activity.AddFoodToMyDailyRecipeListActivity;
import com.example.week_recipe.view.activity.FoodInformationActivity;
import com.example.week_recipe.viewModel.RecipeWithDateViewModel;
import com.example.week_recipe.adapter.FoodListAdapter;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.utility.UiDataCache;
import com.google.android.material.tabs.TabLayout;
import java.time.LocalDate;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.O)
public class RecipeWithDateFragment extends Fragment implements FoodListAdapter.OnFoodListItemClickListener {

    private RecipeWithDateViewModel viewModel;
    private TabLayout tabLayout;
    private DailyRecipeFragment fragment;
    private boolean clickAddButton;
    private boolean clickFoodImage;
    private boolean showAnimation;
    //private RecyclerView foodListRecyclerView;

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_with_date, container, false);
        showAnimation = false;
        fragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_recipeWithDate_fragment));
        tabLayout = view.findViewById(R.id.fragment_recipeWithDate_tabLayout);

        tabLayout.selectTab(tabLayout.getTabAt(1));
        viewModel = new ViewModelProvider(this).get(RecipeWithDateViewModel.class);
        fragment.bind(viewModel.getShowRecipe().getValue(),RecipeWithDateFragment.this,viewModel.getFavouriteFoodList());
        bind();

        return view;
    }

    @Override
    public void onPause() {
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clickAddButton = false;
            clickFoodImage = false;
        }).start();
        super.onPause();
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void bind()
    {
        viewModel.getShowDateText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tabLayout.getTabAt(1).setText(s);
                showAnimation = true;
            }
        });
        viewModel.getShowRecipe().observe(this, new Observer<DailyRecipe>() {
            @Override
            public void onChanged(DailyRecipe dailyRecipe) {
                fragment.updateDailyRecipe(dailyRecipe,showAnimation);
                showAnimation = false;
            }
        });
        viewModel.getFavouriteFoodList().observe(this, new Observer<FoodList>() {
            @Override
            public void onChanged(FoodList foodList) {

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
        fragment.getAddFoodButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddFoodButton();
            }
        });
    }

    private void clickAddFoodButton()
    {
        if (!clickAddButton)
        {
            clickAddButton = true;
            Context context = getContext();
            Intent intent = new Intent(context, AddFoodToMyDailyRecipeListActivity.class);
            intent.putExtra("date",UiDataCache.putData("searchDate",viewModel.getShowDate()));
            intent.putExtra("location",fragment.getTabPosition());
            startActivity(intent);
        }
    }

    @Override
    public void onFoodImageClick(int clickedItemIndex) {
        if (!clickFoodImage)
        {
           clickFoodImage = true;
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
            Intent intent = new Intent(context, FoodInformationActivity.class);
            intent.putExtra("showFood", UiDataCache.putData("showFood",showFood));
            startActivity(intent);
        }
    }

    @Override
    public void onDeleteClick(int clickedItemIndex) {
        viewModel.deleteFood(fragment.getTabPosition(),clickedItemIndex);
    }

    @Override
    public void onLikeClick(int clickedItemIndex) {
        viewModel.changeLikeState(fragment.getTabPosition(),clickedItemIndex);
    }
}