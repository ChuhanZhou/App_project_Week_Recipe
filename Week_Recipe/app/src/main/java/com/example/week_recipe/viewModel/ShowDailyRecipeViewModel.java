package com.example.week_recipe.viewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ShowDailyRecipeViewModel extends ViewModel implements PropertyChangeListener {
    private final SystemModel systemModel;
    private final MutableLiveData<FoodList> basicFoodListForSearch;
    private final MutableLiveData<FoodList> favouriteFoodList;
    private MutableLiveData<DailyRecipe> selectDailyRecipe;
    private int locationOfFoodList;

    public ShowDailyRecipeViewModel()
    {
        systemModel = SystemModelManager.getSystemModelManager();
        basicFoodListForSearch = new MutableLiveData<>();
        favouriteFoodList = new MutableLiveData<>();
        selectDailyRecipe = new MutableLiveData<>();
        //set value
        updateFavouriteFoodList();
        updateBasicFoodListForSearch();
        //add listener
        systemModel.addListener("updateFavoriteFoodList",this);
        systemModel.addListener("updateFood",this);
    }

    private void updateFavouriteFoodList()
    {
        favouriteFoodList.postValue(systemModel.getUserData().getFavoriteFoodList());
    }

    public void updateBasicFoodListForSearch()
    {
        FoodList basic = systemModel.getUserData().getAllFood();
        FoodList foodMenuOfSelect = new FoodList();
        if (selectDailyRecipe.getValue()!=null)
        {
            foodMenuOfSelect = selectDailyRecipe.getValue().getFoodMenu();
        }
        for (int x=0;x<foodMenuOfSelect.getSize();x++)
        {
            basic.add(foodMenuOfSelect.getByIndex(x));
        }
        basicFoodListForSearch.postValue(basic);
    }

    public void setSelectDailyRecipe(DailyRecipe selectDailyRecipe) {
        this.selectDailyRecipe.setValue(selectDailyRecipe);
        updateBasicFoodListForSearch();
    }

    public LiveData<FoodList> getBasicFoodListForSearch() {
        return basicFoodListForSearch;
    }

    public LiveData<FoodList> getFavouriteFoodList() {
        return favouriteFoodList;
    }

    public MutableLiveData<DailyRecipe> getSelectDailyRecipe() {
        return selectDailyRecipe;
    }

    public void changeLikeState(Food food)
    {
        if (basicFoodListForSearch.getValue().hasFood(food))
        {
            food = basicFoodListForSearch.getValue().getByName(food.getName());
        }

        if (food!=null)
        {
            if (systemModel.getUserData().getFavoriteFoodList().hasFood(food))
            {
                systemModel.removeFavoriteFood(food);
            }
            else
            {
                systemModel.addFavoriteFood(food);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateBasicFoodListForSearch();
        updateFavouriteFoodList();
        switch (evt.getPropertyName())
        {
            case "updateFavoriteFoodList":
                break;
            case "updateFood":
                Food oldFood = (Food) evt.getOldValue();
                Food newFood = (Food) evt.getNewValue();
                if (oldFood!=null&&selectDailyRecipe.getValue().getFoodMenu().hasFood(oldFood))
                {
                    selectDailyRecipe.getValue().getFoodMenu().update(oldFood,newFood);
                }
                break;
        }
    }
}
