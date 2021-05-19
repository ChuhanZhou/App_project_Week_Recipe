package com.example.week_recipe.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.view.adapter.WeekRecipeListAdapter;
import com.example.week_recipe.view.fragment.RecipeListFragment;
import com.example.week_recipe.view.fragment.SearchFoodFragment;
import com.example.week_recipe.view.fragment.SearchWeekRecipeFragment;
import com.example.week_recipe.viewModel.SetWeekRecipeByFavouriteViewModel;

import java.time.LocalDate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SetWeekRecipeByFavouriteActivity extends AppCompatActivity implements WeekRecipeListAdapter.OnItemClickListener {
    public static final String valueKey = "setWeekRecipeByFavouriteActivity_firstDateOfWeek";
    private LocalDate firstDateOfWeek;
    private String selectName;
    private ConstraintLayout nameLayout;
    private SearchWeekRecipeFragment searchFragment;
    private RecipeListFragment checkWeekRecipeInfoFragment;
    private TextView nameTextView;
    private Button backButton;
    private Button confirmButton;
    private SetWeekRecipeByFavouriteViewModel viewModel;
    private LiveData<FavouriteWeekRecipeList> basicWeekRecipeList;
    private boolean isSearching;
    private boolean clickFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_week_recipe_by_favourite);
        init();
        bind();
        setListener();
        updateFragment();
    }

    @Override
    public void onBackPressed() {
        if (!isSearching)
        {
            isSearching = true;
            updateFragment();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void init()
    {
        isSearching = true;
        clickFavourite = false;
        viewModel = new ViewModelProvider(this).get(SetWeekRecipeByFavouriteViewModel.class);
        firstDateOfWeek = (LocalDate) UiDataCache.getData(valueKey);
    }

    private void bind()
    {
        searchFragment = FragmentManager.findFragment(findViewById(R.id.setWeekRecipeByFavourite_searchFragment));
        checkWeekRecipeInfoFragment = FragmentManager.findFragment(findViewById(R.id.setWeekRecipeByFavourite_checkWeekRecipeInfoFragment));
        nameLayout = findViewById(R.id.setWeekRecipeByFavourite_nameLayout);
        nameTextView = findViewById(R.id.setWeekRecipeByFavourite_nameTextView);
        backButton = findViewById(R.id.setWeekRecipeByFavourite_backButton);
        confirmButton = findViewById(R.id.setWeekRecipeByFavourite_confirmButton);
        basicWeekRecipeList = viewModel.getBasicWeekRecipeListForSearch();
        searchFragment.bind(basicWeekRecipeList.getValue(),this,false,false);
        checkWeekRecipeInfoFragment.bind(new RecipeList(),null);
    }

    private void setListener()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSearching)
                {
                    isSearching = true;
                    updateFragment();
                }
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickFavourite)
                {
                    clickFavourite = true;
                    String result = viewModel.setWeekRecipeByFavourite(selectName,firstDateOfWeek);
                    if (result!=null)
                    {
                        toastPrint(result);
                        clickFavourite = false;
                    }
                    else
                    {
                        finish();
                    }
                }
            }
        });
        basicWeekRecipeList.observe(this, new Observer<FavouriteWeekRecipeList>() {
            @Override
            public void onChanged(FavouriteWeekRecipeList weekRecipeList) {
                searchFragment.updateBasicFoodList(weekRecipeList,true);
            }
        });
    }

    private void toastPrint(String information)
    {
        if (information!=null)
        {
            Context context = getApplicationContext();
            Toast.makeText(context,information,Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFragment()
    {
        if (isSearching)
        {
            searchFragment.getView().setVisibility(View.VISIBLE);
            checkWeekRecipeInfoFragment.getView().setVisibility(View.GONE);
            confirmButton.setVisibility(View.GONE);
            nameLayout.setVisibility(View.GONE);
        }
        else
        {
            searchFragment.getView().setVisibility(View.GONE);
            checkWeekRecipeInfoFragment.getView().setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(String name) {
        selectName = name;
        isSearching = false;
        updateFragment();
        checkWeekRecipeInfoFragment.updateRecipeList(basicWeekRecipeList.getValue().getByName(name).getRecipeList());
        nameTextView.setText(name);
    }

    @Override
    public void onDeleteClick(String name) {
        //no delete
    }
}