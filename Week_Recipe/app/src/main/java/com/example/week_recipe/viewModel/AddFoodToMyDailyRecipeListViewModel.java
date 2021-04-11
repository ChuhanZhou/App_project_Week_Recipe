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
import java.time.LocalDate;
@RequiresApi(api = Build.VERSION_CODES.O)
public class AddFoodToMyDailyRecipeListViewModel extends ViewModel implements PropertyChangeListener {
    private final SystemModel systemModel;
    private final MutableLiveData<FoodList> basicFoodListForSearch;
    private final MutableLiveData<FoodList> favouriteFoodList;
    private DailyRecipe selectDailyRecipe;
    private int locationOfFoodList;

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

    public void setAddLocation(LocalDate date,int location)
    {
        selectDailyRecipe = new DailyRecipe(date);
        if (systemModel.getUserData().getMyDailyRecipeList().hasDailyRecipe(selectDailyRecipe))
        {
            selectDailyRecipe = systemModel.getUserData().getMyDailyRecipeList().getByDate(date);
        }
        locationOfFoodList = location;
    }

    private void updateFavouriteFoodList()
    {
        favouriteFoodList.setValue(systemModel.getUserData().getFavoriteFoodList());
    }

    private void updateBasicFoodListForSearch()
    {
        basicFoodListForSearch.setValue(systemModel.getUserData().getAllFood());
    }

    public LiveData<FoodList> getBasicFoodListForSearch() {
        return basicFoodListForSearch;
    }

    public LiveData<FoodList> getFavouriteFoodList() {
        return favouriteFoodList;
    }

    public String addFoodToMyDailyRecipeList(Food newFood)
    {
        String result;
        if (selectDailyRecipe==null)
        {
            return "No daily recipe.";
        }
        switch (locationOfFoodList)
        {
            case 0:
                result = selectDailyRecipe.getBreakfast().add(newFood);
                break;
            case 1:
                result = selectDailyRecipe.getLunch().add(newFood);
                break;
            case 2:
                result = selectDailyRecipe.getDinner().add(newFood);
                break;
            default:
                result = "Wrong location of foodList";
        }
        if (result!=null)
        {
            return result;
        }
        return systemModel.updateDailyRecipe(selectDailyRecipe);
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
