package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.week_recipe.R;
import com.example.week_recipe.dao.converter.LocalDateConverter;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.view.activity.FoodInformationActivity;
import com.example.week_recipe.view.adapter.RecipeListAdapter;
import com.example.week_recipe.viewModel.RecipeForWeekViewModel;
import com.example.week_recipe.viewModel.RecipeWithDateViewModel;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RecipeForWeekFragment extends Fragment implements RecipeListAdapter.OnItemClickListener {
    private View view;
    private RecipeForWeekViewModel viewModel;
    private TabLayout tabLayout;
    private RecipeListFragment fragment;
    private PopupCalendarFragment popupCalendarFragment;

    private boolean showCalendar;
    private boolean clickDateTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_for_week, container, false);
        showCalendar = true;
        viewModel = new ViewModelProvider(this).get(RecipeForWeekViewModel.class);
        bind();
        setListener();
        changeVisibilityOfPopupCalenderLayout();
        return view;
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
        fragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_recipeForWeek_fragment));
        tabLayout = view.findViewById(R.id.fragment_recipeForWeek_tabLayout);
        popupCalendarFragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_recipeForWeek_popupCalenderFragment));

        tabLayout.selectTab(tabLayout.getTabAt(1));
        clickDateTab = true;
        fragment.bind(viewModel.getShowRecipeList().getValue(),this);
        popupCalendarFragment.bind(false,viewModel.getFirstDayOfWeek());
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

        viewModel.getShowWeekText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tabLayout.getTabAt(1).setText(s);
                popupCalendarFragment.setDate(viewModel.getFirstDayOfWeek());
            }
        });
        viewModel.getShowRecipeList().observe(this, new Observer<RecipeList>() {
            @Override
            public void onChanged(RecipeList recipeList) {
                fragment.updateRecipeList(recipeList);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        viewModel.setShowDate(viewModel.getFirstDayOfWeek().minusDays(7));
                        clickDateTab = false;
                        break;
                    case 1:

                        break;
                    case 2:
                        viewModel.setShowDate(viewModel.getFirstDayOfWeek().plusDays(7));
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
                if (clickDateTab && tabLayout.getSelectedTabPosition() == 1) {
                    changeVisibilityOfPopupCalenderLayout();
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    clickDateTab = true;
                }
            }
        });
    }

    public void setShowDate(LocalDate date)
    {
        viewModel.setShowDate(date);
    }

    @Override
    public void onItemClick(DailyRecipe dailyRecipe) {
        NavController navController = Navigation.findNavController(view);
        UiDataCache.putData(RecipeWithDateFragment.valueKey,dailyRecipe.getDate());
        navController.navigate(R.id.nav_todayRecipeFragment);
    }
}