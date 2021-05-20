package com.example.week_recipe.viewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipe;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.utility.MyString;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FavouriteWeekRecipeViewModel extends ViewModel implements PropertyChangeListener {

    private final SystemModel systemModel;
    private final MutableLiveData<FavouriteWeekRecipeList> basicWeekRecipeListForSearch;

    public FavouriteWeekRecipeViewModel()
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

    public String updateFavouriteWeekRecipe(FavouriteWeekRecipe oldWeekRecipe,FavouriteWeekRecipe newWeekRecipe)
    {
        if (MyString.isNullOrEmptyOrFullOfSpace(newWeekRecipe.getName()))
        {
            return "The name can't be empty.";
        }
        return systemModel.updateFavoriteWeekRecipe(oldWeekRecipe, newWeekRecipe);
    }

    public void createEmptyRecipe(String title)
    {
        int x=1;
        String emptyTitle = title;
        while (systemModel.getUserData().getFavoriteWeekRecipeList().getByName(emptyTitle)!=null)
        {
            x++;
            emptyTitle = title + x;
        }
        systemModel.addFavoriteWeekRecipe(emptyTitle,new RecipeList());
    }

    public void removeFavouriteWeekRecipe(String name)
    {
        systemModel.removeFavoriteWeekRecipe(name);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateBasicWeekRecipeListForSearch();
    }
}
