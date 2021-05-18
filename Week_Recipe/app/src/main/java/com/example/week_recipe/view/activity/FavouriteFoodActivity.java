package com.example.week_recipe.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.view.fragment.AddFoodToFoodListFragment;
import com.example.week_recipe.view.fragment.SearchFoodFragment;
import com.example.week_recipe.viewModel.AddFoodToMyDailyRecipeListViewModel;
import com.example.week_recipe.viewModel.FavouriteFoodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FavouriteFoodActivity extends AppCompatActivity implements AddFoodToFoodListFragment.StateListener {

    private SearchFoodFragment showFavouriteFragment;
    private AddFoodToFoodListFragment addFavouriteFoodFragment;
    private FavouriteFoodViewModel viewModel;
    private FloatingActionButton addFoodButton;
    private LiveData<FoodList> favouriteFoodList;
    private LiveData<FoodList> allFoodList;
    private boolean isAdding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_food);
        init();
        bind();
        setListener();
        updateFragment();
    }

    @Override
    public void onBackPressed() {
        if (isAdding)
        {
            if (addFavouriteFoodFragment.onBackPressed())
            {
                isAdding = false;
                updateFragment();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void toastPrint(String information)
    {
        if (information!=null)
        {
            Context context = getApplicationContext();
            Toast.makeText(context,information,Toast.LENGTH_SHORT).show();
        }
    }

    private void init()
    {
        isAdding = false;
        viewModel = new ViewModelProvider(this).get(FavouriteFoodViewModel.class);
        favouriteFoodList = viewModel.getFavouriteFoodList();
        allFoodList = viewModel.getAllFoodList();
    }

    private void bind()
    {
        showFavouriteFragment = FragmentManager.findFragment(findViewById(R.id.favouriteFoodActivity_showFavouriteFragment));
        addFavouriteFoodFragment = FragmentManager.findFragment(findViewById(R.id.favouriteFoodActivity_addFavouriteFoodFragment));
        addFoodButton = findViewById(R.id.favouriteFoodActivity_addFoodButton);

        showFavouriteFragment.bind(favouriteFoodList.getValue(),this,true,true,false,favouriteFoodList);
        addFavouriteFoodFragment.bind(allFoodList,favouriteFoodList,this);
    }

    private void setListener()
    {
        favouriteFoodList.observe(this, new Observer<FoodList>() {
            @Override
            public void onChanged(FoodList foodList) {
                showFavouriteFragment.updateBasicFoodList(foodList);
            }
        });
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdding)
                {
                    isAdding = true;
                    updateFragment();
                }
            }
        });
    }

    private void updateFragment()
    {
        if (!isAdding)
        {
            showFavouriteFragment.getView().setVisibility(View.VISIBLE);
            addFoodButton.setVisibility(View.VISIBLE);
            addFavouriteFoodFragment.getView().setVisibility(View.GONE);
        }
        else
        {
            showFavouriteFragment.getView().setVisibility(View.GONE);
            addFoodButton.setVisibility(View.GONE);
            addFavouriteFoodFragment.getView().setVisibility(View.VISIBLE);
            addFavouriteFoodFragment.updateFragment(true);
            addFavouriteFoodFragment.clearSearchText();
        }
    }

    @Override
    public void createFood(Food newFood) {
        if (isAdding)
        {
            String result = viewModel.addFavoriteFood(newFood);
            if (result==null)
            {
                isAdding = false;
                updateFragment();
            }
            toastPrint(result);
        }
    }

    @Override
    public void onFoodImageClick(int clickedItemIndex) {
        if (!isAdding)
        {
            Context context = getApplicationContext();
            Intent intent = new Intent(context, FoodInformationActivity.class);
            intent.putExtra("showFood", UiDataCache.putData("showFood",showFavouriteFragment.getShowList().getByIndex(clickedItemIndex)));
            startActivity(intent);
        }
        else
        {
            String result = viewModel.addFavoriteFood(addFavouriteFoodFragment.getShowList().getByIndex(clickedItemIndex));
            if (result==null)
            {
                isAdding = false;
                updateFragment();
            }
            toastPrint(result);
        }
    }

    @Override
    public void onDeleteClick(int clickedItemIndex) {
        if (!isAdding)
        {
            viewModel.removeFavoriteFood(showFavouriteFragment.getShowList().getByIndex(clickedItemIndex).getName());
        }
    }

    @Override
    public void onLikeClick(int clickedItemIndex) {
        //no event
    }
}