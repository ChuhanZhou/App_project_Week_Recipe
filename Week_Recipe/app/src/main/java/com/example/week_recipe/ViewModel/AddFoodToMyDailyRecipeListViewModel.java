package com.example.week_recipe.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.FoodList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AddFoodToMyDailyRecipeListViewModel extends ViewModel implements PropertyChangeListener {
    private final SystemModel systemModel;
    private final MutableLiveData<FoodList> basicFoodListForSearch;
    private final MutableLiveData<FoodList> favouriteFoodList;

    public AddFoodToMyDailyRecipeListViewModel()
    {
        systemModel = SystemModelManager.getSystemModelManager();
        basicFoodListForSearch = new MutableLiveData<>();
        favouriteFoodList = new MutableLiveData<>();
        //set value
        updateFavouriteFoodList();
        updateBasicFoodListForSearch();
        //add listener
        systemModel.addListener("updateDailyRecipeList",this);
        systemModel.addListener("updateFavoriteFoodList",this);
        systemModel.addListener("updateFood",this);
    }

    private void updateFavouriteFoodList()
    {
        favouriteFoodList.setValue(systemModel.getUserData().getFavoriteFoodList());
    }

    private void updateBasicFoodListForSearch()
    {
        FoodList basicFoodList = new FoodList();
        FoodList favouriteFoodList = this.favouriteFoodList.getValue();
        FoodList MenuOfDailyRecipeList = systemModel.getUserData().getMyDailyRecipeList().getFoodMenu();
        for (int x=0;x<favouriteFoodList.getSize();x++)
        {
            basicFoodList.add(favouriteFoodList.getByIndex(x));
        }
        for (int x=0;x<MenuOfDailyRecipeList.getSize();x++)
        {
            basicFoodList.add(MenuOfDailyRecipeList.getByIndex(x));
        }
        basicFoodListForSearch.setValue(basicFoodList);
    }

    public LiveData<FoodList> getBasicFoodListForSearch() {
        return basicFoodListForSearch;
    }

    public LiveData<FoodList> getFavouriteFoodList() {
        return favouriteFoodList;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateBasicFoodListForSearch();
        if (evt.getPropertyName().equals("updateFavoriteFoodList")||evt.getPropertyName().equals("updateFood"))
        {
            updateFavouriteFoodList();
        }
    }
}
