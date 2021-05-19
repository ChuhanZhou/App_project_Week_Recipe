package com.example.week_recipe.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipe;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.view.adapter.RecipeListAdapter;
import com.example.week_recipe.view.adapter.WeekRecipeListAdapter;
import com.example.week_recipe.view.fragment.RecipeListFragment;
import com.example.week_recipe.view.fragment.SearchWeekRecipeFragment;
import com.example.week_recipe.viewModel.FavouriteWeekRecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FavouriteWeekRecipeActivity extends AppCompatActivity implements WeekRecipeListAdapter.OnItemClickListener, RecipeListAdapter.OnItemClickListener {

    private FavouriteWeekRecipe oldWeekRecipe;
    private FavouriteWeekRecipe newWeekRecipe;
    private ConstraintLayout nameLayout;
    private SearchWeekRecipeFragment showFavouriteFragment;
    private RecipeListFragment checkWeekRecipeInfoFragment;
    private EditText nameEditText;
    private Button backButton;
    private FloatingActionButton saveButton;
    private FavouriteWeekRecipeViewModel viewModel;
    private LiveData<FavouriteWeekRecipeList> basicWeekRecipeList;
    private boolean isChecking;
    private boolean showAnimation;

    private ArrayList<Integer> getDateTitleIdList()
    {
        ArrayList<Integer> titleDateIdList = new ArrayList<>();
        titleDateIdList.add(R.string.text_monday);
        titleDateIdList.add(R.string.text_tuesday);
        titleDateIdList.add(R.string.text_wednesday);
        titleDateIdList.add(R.string.text_thursday);
        titleDateIdList.add(R.string.text_friday);
        titleDateIdList.add(R.string.text_saturday);
        titleDateIdList.add(R.string.text_sunday);
        return titleDateIdList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_week_recipe);
        init();
        bind();
        setListener();
        updateFragment();
    }

    @Override
    public void onBackPressed() {
        if (isChecking)
        {
            isChecking = false;
            updateFragment();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isChecking)
        {
            checkWeekRecipeInfoFragment.updateRecipeList(newWeekRecipe.getRecipeList());
        }
    }

    private void init()
    {
        isChecking = false;
        showAnimation = true;
        viewModel = new ViewModelProvider(this).get(FavouriteWeekRecipeViewModel.class);
    }

    private void bind()
    {
        showFavouriteFragment = FragmentManager.findFragment(findViewById(R.id.favouriteWeekRecipe_showFavouriteFragment));
        checkWeekRecipeInfoFragment = FragmentManager.findFragment(findViewById(R.id.favouriteWeekRecipe_checkWeekRecipeInfoFragment));
        nameLayout = findViewById(R.id.favouriteWeekRecipe_nameLayout);
        nameEditText = findViewById(R.id.favouriteWeekRecipe_nameEditText);
        backButton = findViewById(R.id.favouriteWeekRecipe_backButton);
        saveButton = findViewById(R.id.favouriteWeekRecipe_saveButton);
        basicWeekRecipeList = viewModel.getBasicWeekRecipeListForSearch();
        showFavouriteFragment.bind(basicWeekRecipeList.getValue(),this,true,true);
        checkWeekRecipeInfoFragment.bind(new RecipeList(),this);
    }

    private void setListener()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecking)
                {
                    isChecking = false;
                    updateFragment();
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecking)
                {
                    String result = viewModel.updateFavouriteWeekRecipe(oldWeekRecipe,newWeekRecipe);
                    if (result!=null)
                    {
                        toastPrint(result);
                    }
                    else
                    {
                        isChecking = false;
                        updateFragment();
                    }
                }
            }
        });
        basicWeekRecipeList.observe(this, new Observer<FavouriteWeekRecipeList>() {
            @Override
            public void onChanged(FavouriteWeekRecipeList weekRecipeList) {
                showFavouriteFragment.updateBasicFoodList(weekRecipeList,showAnimation);
                showAnimation = false;
            }
        });
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newWeekRecipe.setName(s.toString());
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
        if (!isChecking)
        {
            showFavouriteFragment.getView().setVisibility(View.VISIBLE);
            checkWeekRecipeInfoFragment.getView().setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            nameLayout.setVisibility(View.GONE);
        }
        else
        {
            showFavouriteFragment.getView().setVisibility(View.GONE);
            checkWeekRecipeInfoFragment.getView().setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(String name) {
        if (!isChecking)
        {
            oldWeekRecipe = showFavouriteFragment.getShowList().getByName(name);
            newWeekRecipe = oldWeekRecipe.copy();
            isChecking = true;
            updateFragment();
            checkWeekRecipeInfoFragment.updateRecipeList(newWeekRecipe.getRecipeList());
            nameEditText.setText(name);
        }
    }

    @Override
    public void onDeleteClick(String name) {
        if (!isChecking)
        {
            viewModel.removeFavouriteWeekRecipe(name);
        }
    }

    @Override
    public void onItemClick(int position,DailyRecipe dailyRecipe) {
        if (isChecking)
        {
            Context context = getApplicationContext();
            Intent intent = new Intent(context, ShowDailyRecipeActivity.class);
            intent.putExtra(ShowDailyRecipeActivity.dateTitleKey,getText(getDateTitleIdList().get(position)));
            UiDataCache.putData(ShowDailyRecipeActivity.showDailyRecipeKey,dailyRecipe);
            startActivity(intent);
        }
    }
}