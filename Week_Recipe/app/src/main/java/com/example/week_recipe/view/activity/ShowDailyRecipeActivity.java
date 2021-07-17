package com.example.week_recipe.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.view.adapter.FoodListAdapter;
import com.example.week_recipe.view.fragment.AddFoodToFoodListFragment;
import com.example.week_recipe.view.fragment.DailyRecipeFragment;
import com.example.week_recipe.viewModel.SetWeekRecipeByFavouriteViewModel;
import com.example.week_recipe.viewModel.ShowDailyRecipeViewModel;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ShowDailyRecipeActivity extends AppCompatActivity implements FoodListAdapter.OnFoodListItemClickListener, AddFoodToFoodListFragment.StateListener {
    public static String showDailyRecipeKey = "showDailyRecipeActivity_showDailyRecipe";
    public static String dateTitleKey = "showDailyRecipeActivity_dateTitle";
    private ConstraintLayout nameLayout;
    private Button backButton;
    private TextView dateTitleTextView;
    private DailyRecipeFragment dailyRecipeFragment;
    private AddFoodToFoodListFragment addFoodToFoodListFragment;
    private ShowDailyRecipeViewModel viewModel;
    private boolean isAdding;
    private boolean clickFoodImage;
    private MutableLiveData<DailyRecipe> dailyRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_daily_recipe);
        init();
        bind();
        setListener();
        updateFragment();
    }

    @Override
    public void onBackPressed() {
        if (isAdding)
        {
            if (addFoodToFoodListFragment.onBackPressed())
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

    @Override
    protected void onPause() {
        super.onPause();
        clickFoodImage = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateDailyRecipe();
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
        viewModel = new ViewModelProvider(this).get(ShowDailyRecipeViewModel.class);
    }

    private void bind()
    {
        nameLayout = findViewById(R.id.showDailyRecipe_nameLayout);
        backButton = findViewById(R.id.showDailyRecipe_backButton);
        dateTitleTextView = findViewById(R.id.showDailyRecipe_dateTitleTextView);
        dailyRecipeFragment = FragmentManager.findFragment(findViewById(R.id.showDailyRecipe_dailyRecipeFragment));
        addFoodToFoodListFragment = FragmentManager.findFragment(findViewById(R.id.showDailyRecipe_addFoodToFoodListFragment));
        viewModel.setSelectDailyRecipe((DailyRecipe) UiDataCache.getData(showDailyRecipeKey));
        dailyRecipe = viewModel.getSelectDailyRecipe();

        dateTitleTextView.setText(getIntent().getExtras().getString(dateTitleKey));
        dailyRecipeFragment.bind(dailyRecipe.getValue(),0,this,viewModel.getFavouriteFoodList());
    }

    private void setListener()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdding)
                {
                    onBackPressed();
                }
            }
        });
        dailyRecipeFragment.getAddFoodButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdding = true;
                updateFragment();
            }
        });
        viewModel.getSelectDailyRecipe().observe(this, new Observer<DailyRecipe>() {
            @Override
            public void onChanged(DailyRecipe dailyRecipe) {
                viewModel.updateBasicFoodListForSearch();
            }
        });
    }

    private void updateDailyRecipe()
    {
        dailyRecipe.setValue(dailyRecipe.getValue());
        dailyRecipeFragment.updateDailyRecipe(dailyRecipe.getValue(),false);
    }

    private void updateFragment()
    {
        if (!isAdding)
        {
            dailyRecipeFragment.getView().setVisibility(View.VISIBLE);
            addFoodToFoodListFragment.getView().setVisibility(View.GONE);
            nameLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            dailyRecipeFragment.getView().setVisibility(View.GONE);
            addFoodToFoodListFragment.getView().setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.GONE);
            addFoodToFoodListFragment.bind(viewModel.getBasicFoodListForSearch(),viewModel.getFavouriteFoodList(),this);
        }
    }

    @Override
    public void onFoodImageClick(int clickedItemIndex) {
        if (!isAdding)
        {
            if (!clickFoodImage)
            {
                clickFoodImage = true;
                Food showFood = null;
                switch (dailyRecipeFragment.getTabPosition())
                {
                    case 0:
                        showFood = dailyRecipe.getValue().getBreakfast().getByIndex(clickedItemIndex);
                        break;
                    case 1:
                        showFood = dailyRecipe.getValue().getLunch().getByIndex(clickedItemIndex);
                        break;
                    case 2:
                        showFood = dailyRecipe.getValue().getDinner().getByIndex(clickedItemIndex);
                        break;
                }
                Context context = getApplicationContext();
                Intent intent = new Intent(context, FoodInformationActivity.class);
                intent.putExtra("showFood", UiDataCache.putData("showFood",showFood));
                UiDataCache.putData(FoodInformationActivity.foodMenuKey,viewModel.getBasicFoodListForSearch().getValue());
                startActivity(intent);
            }
        }
        else
        {
            String result = "";
            Food selectFood = addFoodToFoodListFragment.getShowList().getByIndex(clickedItemIndex);
            switch (dailyRecipeFragment.getTabPosition())
            {
                case 0:
                    result = dailyRecipe.getValue().getBreakfast().add(selectFood);
                    break;
                case 1:
                    result = dailyRecipe.getValue().getLunch().add(selectFood);
                    break;
                case 2:
                    result = dailyRecipe.getValue().getDinner().add(selectFood);
                    break;
            }
            if (result!=null)
            {
                toastPrint(result);
            }
            else
            {
                isAdding = false;
                updateFragment();
                dailyRecipeFragment.updateDailyRecipe(dailyRecipe.getValue(),true);
                updateDailyRecipe();
            }
        }
    }

    @Override
    public void onDeleteClick(int clickedItemIndex) {
        if (!isAdding)
        {
            switch (dailyRecipeFragment.getTabPosition())
            {
                case 0:
                    dailyRecipe.getValue().getBreakfast().remove(clickedItemIndex);
                    break;
                case 1:
                    dailyRecipe.getValue().getLunch().remove(clickedItemIndex);
                    break;
                case 2:
                    dailyRecipe.getValue().getDinner().remove(clickedItemIndex);
                    break;
            }
            dailyRecipeFragment.updateDailyRecipe(dailyRecipe.getValue(),false);
            updateDailyRecipe();
        }
    }

    @Override
    public void onLikeClick(int clickedItemIndex) {
        toastPrint("Can't change favourite state.");
    }

    @Override
    public void createFood(Food newFood) {
        String result = "";
        switch (dailyRecipeFragment.getTabPosition())
        {
            case 0:
                result = dailyRecipe.getValue().getBreakfast().add(newFood);
                break;
            case 1:
                result = dailyRecipe.getValue().getLunch().add(newFood);
                break;
            case 2:
                result = dailyRecipe.getValue().getDinner().add(newFood);
                break;
        }
        if (result!=null)
        {
            toastPrint(result);
        }
        else
        {
            isAdding = false;
            updateFragment();
            dailyRecipeFragment.updateDailyRecipe(dailyRecipe.getValue(),true);
            updateDailyRecipe();
        }
    }
}