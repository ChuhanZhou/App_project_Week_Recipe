package com.example.week_recipe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.week_recipe.ViewModel.AddFoodToMyDailyRecipeListViewModel;
import com.example.week_recipe.ViewModel.EditFoodInformationViewModel;
import com.example.week_recipe.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.utility.UiDataCache;
import com.google.gson.Gson;

import java.time.LocalDate;

public class AddFoodToMyDailyRecipeListActivity extends AppCompatActivity implements FoodListAdapter.OnFoodListItemClickListener {

    private SearchFoodFragment searchFragment;
    private SetFoodInformationFragment setFoodInformationFragment;
    private TextView createFoodTextView;
    private AddFoodToMyDailyRecipeListViewModel viewModel;
    private LiveData<FoodList> basicFoodList;
    private LiveData<FoodList> favouriteFoodList;
    private boolean isSearching;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_to_my_daily_recipe_list);

        isSearching = true;
        searchFragment = FragmentManager.findFragment(findViewById(R.id.addFoodToMyDailyRecipeList_searchFragment));
        setFoodInformationFragment = FragmentManager.findFragment(findViewById(R.id.addFoodToMyDailyRecipeList_setFoodInformationFragment));
        createFoodTextView = findViewById(R.id.addFoodToMyDailyRecipeList_createFoodTextView);
        viewModel = new ViewModelProvider(this).get(AddFoodToMyDailyRecipeListViewModel.class);
        viewModel.setAddLocation((LocalDate) UiDataCache.getData(getIntent().getExtras().getString("date")),getIntent().getExtras().getInt("location"));
        basicFoodList = viewModel.getBasicFoodListForSearch();
        favouriteFoodList = viewModel.getFavouriteFoodList();

        searchFragment.bind(basicFoodList.getValue(),this,false,false,true,favouriteFoodList);
        updateFragment();
        updateCreateFoodTextViewVisibility();
        setListener();
    }

    private void toastPrint(String information)
    {
        Context context = getApplicationContext();
        Toast.makeText(context,information,Toast.LENGTH_SHORT).show();
    }

    private void setListener()
    {
        basicFoodList.observe(this, new Observer<FoodList>() {
            @Override
            public void onChanged(FoodList foodList) {
                searchFragment.updateBasicFoodList(foodList);
            }
        });
        createFoodTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFood();
            }
        });
        searchFragment.getFoodNameEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCreateFoodTextViewVisibility();
            }
        });
    }

    private void updateCreateFoodTextViewVisibility()
    {
        if (isSearching)
        {
            if (searchFragment.foodNameCanBeUsed())
            {
                showCreateFoodTextView();
            }
            else
            {
                hidCreateFoodTextView();
            }
        }
        else
        {
            showCreateFoodTextView();
        }
    }

    private void showCreateFoodTextView()
    {
        if (createFoodTextView.getVisibility()==View.GONE)
        {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            translateAnimation.setDuration(200);
            createFoodTextView.setAnimation(translateAnimation);
            createFoodTextView.setVisibility(View.VISIBLE);
        }
    }

    private void hidCreateFoodTextView()
    {
        if (createFoodTextView.getVisibility()==View.VISIBLE)
        {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);
            translateAnimation.setDuration(200);
            createFoodTextView.setAnimation(translateAnimation);
            createFoodTextView.setVisibility(View.GONE);
        }
    }

    private void updateFragment()
    {
        if (isSearching)
        {
            searchFragment.getView().setVisibility(View.VISIBLE);
            setFoodInformationFragment.getView().setVisibility(View.GONE);
        }
        else
        {
            searchFragment.getView().setVisibility(View.GONE);
            setFoodInformationFragment.getView().setVisibility(View.VISIBLE);
        }
    }

    private void createFood()
    {
        if (isSearching)
        {

        }
        else
        {

        }
    }

    @Override
    public void onFoodImageClick(int clickedItemIndex) {
        String result = viewModel.addFoodToMyDailyRecipeList(searchFragment.getShowList().getByIndex(clickedItemIndex));
        if (result!=null)
        {
            toastPrint(result);
        }
        else
        {
            finish();
        }
    }

    @Override
    public void onDeleteClick(int clickedItemIndex) {
        //no delete
    }

    @Override
    public void onLikeClick(int clickedItemIndex) {

    }
}