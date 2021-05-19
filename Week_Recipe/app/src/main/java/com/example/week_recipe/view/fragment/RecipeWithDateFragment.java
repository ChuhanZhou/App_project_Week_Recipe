package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
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
import com.example.week_recipe.dao.converter.LocalDateConverter;
import com.example.week_recipe.view.activity.AddFoodToMyDailyRecipeListActivity;
import com.example.week_recipe.view.activity.FoodInformationActivity;
import com.example.week_recipe.viewModel.RecipeWithDateViewModel;
import com.example.week_recipe.view.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.utility.UiDataCache;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RecipeWithDateFragment extends Fragment implements FoodListAdapter.OnFoodListItemClickListener {

    public static final String valueKey = "recipeWithDateFragment_showDate";
    private View view;
    private RecipeWithDateViewModel viewModel;
    private TabLayout tabLayout;
    private DailyRecipeFragment fragment;
    private PopupCalendarFragment popupCalendarFragment;

    private boolean clickAddButton;
    private boolean clickFoodImage;
    private boolean showAnimation;
    private boolean showCalendar;
    private boolean clickDateTab;

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_with_date, container, false);
        showAnimation = false;
        showCalendar = true;
        viewModel = new ViewModelProvider(this).get(RecipeWithDateViewModel.class);
        if (UiDataCache.getData(valueKey)!=null)
        {
            viewModel.setShowDate((LocalDate) UiDataCache.getData(valueKey));
        }
        bind();
        setListener();
        changeVisibilityOfPopupCalenderLayout();
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

    private void changeVisibilityOfPopupCalenderLayout()
    {
        if (showCalendar)
        {
            tabLayout.setTranslationZ(0f);
            showCalendar = false;
            popupCalendarFragment.getView().setVisibility(View.GONE);
        }
        else
        {
            tabLayout.setTranslationZ(5f);
            showCalendar = true;
            popupCalendarFragment.getView().setVisibility(View.VISIBLE);
            popupCalendarFragment.showCalendar(0.25);
        }
    }

    private void bind()
    {
        fragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_recipeWithDate_fragment));
        tabLayout = view.findViewById(R.id.fragment_recipeWithDate_tabLayout);
        popupCalendarFragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_recipeWithDate_popupCalenderFragment));

        tabLayout.selectTab(tabLayout.getTabAt(1));
        clickDateTab = true;
        int tabPosition = 0;
        if (LocalDateConverter.localDateToString(viewModel.getShowDate()).equals(LocalDateConverter.localDateToString(LocalDate.now())))
        {
            if (LocalTime.now().isBefore(LocalTime.of(9,0)))
            {
                tabPosition = 0;
            }
            else if (LocalTime.now().isBefore(LocalTime.of(14,0)))
            {
                tabPosition = 1;
            }
            else
            {
                tabPosition = 2;
            }
        }
        fragment.bind(viewModel.getShowRecipe().getValue(),tabPosition,RecipeWithDateFragment.this,viewModel.getFavouriteFoodList());
        popupCalendarFragment.bind(true,viewModel.getShowDate());
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void setListener()
    {
        popupCalendarFragment.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibilityOfPopupCalenderLayout();
            }
        });
        popupCalendarFragment.getDate().observe(this, new Observer<LocalDate>() {
            @Override
            public void onChanged(LocalDate date) {
                viewModel.setShowDate(date);
            }
        });
        viewModel.getShowDateText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                UiDataCache.putData(valueKey,viewModel.getShowDate());
                tabLayout.getTabAt(1).setText(s);
                popupCalendarFragment.setDate(viewModel.getShowDate());
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
                        clickDateTab = false;
                        break;
                    case 1:

                        break;
                    case 2:
                        viewModel.setShowDate(viewModel.getShowDate().plusDays(1));
                        clickDateTab = false;
                        break;
                }
                tabLayout.selectTab(tabLayout.getTabAt(1));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (clickDateTab&&tabLayout.getSelectedTabPosition()==1)
                {
                    changeVisibilityOfPopupCalenderLayout();
                }
                else if (tabLayout.getSelectedTabPosition()==1)
                {
                    clickDateTab = true;
                }
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

    public void setShowDate(LocalDate date)
    {
        viewModel.setShowDate(date);
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
            UiDataCache.putData(FoodInformationActivity.foodMenuKey,null);
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