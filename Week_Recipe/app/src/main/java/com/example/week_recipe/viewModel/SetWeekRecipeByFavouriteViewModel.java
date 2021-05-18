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
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipe;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.view.fragment.SearchWeekRecipeFragment;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SetWeekRecipeByFavouriteViewModel extends ViewModel implements PropertyChangeListener {

    private final SystemModel systemModel;
    private final MutableLiveData<FavouriteWeekRecipeList> basicWeekRecipeListForSearch;
    private FavouriteWeekRecipe selectWeekRecipe;

    public SetWeekRecipeByFavouriteViewModel()
    {
        systemModel = SystemModelManager.getSystemModelManager();
        basicWeekRecipeListForSearch = new MutableLiveData<>();
        //set value
        updateBasicWeekRecipeListForSearch();
        //add listener
        systemModel.addListener("updateFavoriteWeekRecipe",this);
        systemModel.addListener("updateFood",this);
    }

    public LiveData<FavouriteWeekRecipeList> getBasicWeekRecipeListForSearch() {
        return basicWeekRecipeListForSearch;
    }

    private void updateBasicWeekRecipeListForSearch()
    {
        basicWeekRecipeListForSearch.setValue(systemModel.getUserData().getFavoriteWeekRecipeList());
    }

    public String setWeekRecipeByFavourite(String name, LocalDate dayOfWeek)
    {
        selectWeekRecipe = systemModel.getUserData().getFavoriteWeekRecipeList().getByName(name);
        if (selectWeekRecipe==null)
        {
            return "No favourite week recipe ["+name+"].";
        }
        return systemModel.addDailyRecipeList(selectWeekRecipe.getRecipeList(),dayOfWeek);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateBasicWeekRecipeListForSearch();
    }
}
