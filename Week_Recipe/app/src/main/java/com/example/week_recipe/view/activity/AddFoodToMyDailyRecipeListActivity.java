package com.example.week_recipe.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.view.fragment.AddFoodToFoodListFragment;
import com.example.week_recipe.viewModel.AddFoodToMyDailyRecipeListViewModel;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.utility.UiDataCache;

import java.time.LocalDate;
@RequiresApi(api = Build.VERSION_CODES.O)
public class AddFoodToMyDailyRecipeListActivity extends AppCompatActivity implements AddFoodToFoodListFragment.StateListener {

    private AddFoodToFoodListFragment addFoodToFoodListFragment;
    private AddFoodToMyDailyRecipeListViewModel viewModel;
    private LiveData<FoodList> basicFoodList;
    private LiveData<FoodList> favouriteFoodList;
    private boolean clickFoodImage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_to_my_daily_recipe_list);

        clickFoodImage = false;
        addFoodToFoodListFragment = FragmentManager.findFragment(findViewById(R.id.addFoodToMyDailyRecipeList_addFoodToFoodListFragment));

        viewModel = new ViewModelProvider(this).get(AddFoodToMyDailyRecipeListViewModel.class);
        viewModel.setAddLocation((LocalDate) UiDataCache.getData(getIntent().getExtras().getString("date")),getIntent().getExtras().getInt("location"));
        basicFoodList = viewModel.getBasicFoodListForSearch();
        favouriteFoodList = viewModel.getFavouriteFoodList();

        addFoodToFoodListFragment.bind(basicFoodList,favouriteFoodList,this);
    }

    @Override
    public void onBackPressed() {
        if (addFoodToFoodListFragment.onBackPressed())
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

    @Override
    public void onFoodImageClick(int clickedItemIndex) {
        if (!clickFoodImage)
        {
            clickFoodImage = true;
            String result = viewModel.addFoodToMyDailyRecipeList(addFoodToFoodListFragment.getShowList().getByIndex(clickedItemIndex));
            if (result!=null)
            {
                toastPrint(result);
                clickFoodImage = false;
            }
            else
            {
                finish();
            }
        }
    }

    @Override
    public void onDeleteClick(int clickedItemIndex) {
        //no delete
    }

    @Override
    public void onLikeClick(int clickedItemIndex) {

    }

    @Override
    public void createFood(Food newFood) {
        String result = viewModel.addFoodToMyDailyRecipeList(newFood);
        if (result!=null)
        {
            toastPrint(result);
        }
        else
        {
            finish();
        }
    }
}