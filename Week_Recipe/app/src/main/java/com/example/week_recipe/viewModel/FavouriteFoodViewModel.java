package com.example.week_recipe.viewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FavouriteFoodViewModel extends ViewModel implements PropertyChangeListener {
    private final SystemModel systemModel;
    private MutableLiveData<FoodList> favouriteFoodList;
    private MutableLiveData<FoodList> allFoodList;

    public FavouriteFoodViewModel()
    {
        systemModel = SystemModelManager.getSystemModelManager();
        favouriteFoodList = new MutableLiveData<>();
        allFoodList = new MutableLiveData<>();

        updateFavouriteFoodList();
        updateAllFoodList();

        systemModel.addListener("updateDailyRecipeList",this);
        systemModel.addListener("updateFavoriteFoodList",this);
        systemModel.addListener("updateFood",this);
    }

    private void updateFavouriteFoodList()
    {
        favouriteFoodList.setValue(systemModel.getUserData().getFavoriteFoodList());
    }

    private void updateAllFoodList()
    {
        allFoodList.setValue(systemModel.getUserData().getAllFood());
    }

    public LiveData<FoodList> getAllFoodList() {
        return allFoodList;
    }

    public LiveData<FoodList> getFavouriteFoodList() {
        return favouriteFoodList;
    }

    public String addFavoriteFood(Food newFood)
    {
        if (systemModel.getUserData().getAllFood().hasFood(newFood))
        {
            newFood = systemModel.getUserData().getAllFood().getByName(newFood.getName());
        }

        return systemModel.addFavoriteFood(newFood);
    }

    public void removeFavoriteFood(String foodName)
    {
        Food food = systemModel.getUserData().getAllFood().getByName(foodName);
        systemModel.removeFavoriteFood(food);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateAllFoodList();
        if (evt.getPropertyName().equals("updateFavoriteFoodList")||evt.getPropertyName().equals("updateFood")||evt.getPropertyName().equals("setUserData"))
        {
            updateFavouriteFoodList();
        }
    }
}
