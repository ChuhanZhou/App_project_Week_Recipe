package com.example.week_recipe.model.domain.recipe;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.utility.MyString;

import java.util.ArrayList;

public class FavouriteWeekRecipeList {
    private ArrayList<FavouriteWeekRecipe> favouriteWeekRecipeList;

    public FavouriteWeekRecipeList() {
        favouriteWeekRecipeList = new ArrayList<>();
    }

    private boolean hasFavoriteWeekRecipeName(String name) {
        for (int x = 0; x < favouriteWeekRecipeList.size(); x++) {
            if (favouriteWeekRecipeList.get(x).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return favouriteWeekRecipeList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String add(String name, RecipeList newRecipeList) {
        if (!hasFavoriteWeekRecipeName(name))
        {
            if (newRecipeList != null)
            {
                if (newRecipeList.getSize() <= 7)
                {
                    favouriteWeekRecipeList.add(new FavouriteWeekRecipe(name, newRecipeList));
                    return null;
                }
                return "The daily recipe should less than 8.";
            }
            return "Input null!";
        }
        return "The name [" + name + "] is used";
    }

    public String add(FavouriteWeekRecipe newWeekRecipe) {
        if (newWeekRecipe != null)
        {
            if (!hasFavoriteWeekRecipeName(newWeekRecipe.getName()))
            {
                if (newWeekRecipe.getRecipeList().getSize() <= 7)
                {
                    favouriteWeekRecipeList.add(newWeekRecipe);
                    return null;
                }
                return "The size of week recipe should less than 8.";
            }
            return "The name [" + newWeekRecipe.getName() + "] is used";
        }
        return "Input null!";
    }

    public FavouriteWeekRecipe getByIndex(int index) {
        if (index >= 0 && index < favouriteWeekRecipeList.size()) {
            return favouriteWeekRecipeList.get(index);
        }
        return null;
    }

    public FavouriteWeekRecipe getByName(String name) {
        for (int x = 0; x < favouriteWeekRecipeList.size(); x++) {
            if (favouriteWeekRecipeList.get(x).getName().equals(name)) {
                return favouriteWeekRecipeList.get(x);
            }
        }
        return null;
    }

    public FavouriteWeekRecipeList getListByName(String name) {
        FavouriteWeekRecipeList searchList = new FavouriteWeekRecipeList();
        for (int x = 0; x < favouriteWeekRecipeList.size(); x++) {
            if (MyString.haveOrInside(name, favouriteWeekRecipeList.get(x).getName())) {
                searchList.add(favouriteWeekRecipeList.get(x));
            }
        }
        return searchList;
    }

    public String update(FavouriteWeekRecipe oldRecipeList, FavouriteWeekRecipe newRecipeList) {
        if (oldRecipeList != null && newRecipeList != null) {
            for (int x = 0; x < favouriteWeekRecipeList.size(); x++) {
                if (favouriteWeekRecipeList.get(x).getName().equals(oldRecipeList.getName())) {
                    if (!oldRecipeList.getName().equals(newRecipeList.getName()) && hasFavoriteWeekRecipeName(newRecipeList.getName())) {
                        return "The new name [" + newRecipeList.getName() + "] is used";
                    } else {
                        if (newRecipeList.getRecipeList().getSize() <= 7) {
                            favouriteWeekRecipeList.set(x, newRecipeList);
                            return null;
                        }
                    }
                    return "The daily recipe should less than 8.";
                }
            }
            return "Can't find old Recipe List.";
        }
        return "Input null!";
    }

    public void removeByName(String name) {
        for (int x = 0; x < favouriteWeekRecipeList.size(); x++) {
            if (favouriteWeekRecipeList.get(x).getName().equals(name)) {
                favouriteWeekRecipeList.remove(x);
                break;
            }
        }
    }

    public void removeByIndex(int index) {
        favouriteWeekRecipeList.remove(index);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FavouriteWeekRecipeList copy() {
        FavouriteWeekRecipeList copyList = new FavouriteWeekRecipeList();
        for (int x = 0; x < favouriteWeekRecipeList.size(); x++) {
            copyList.favouriteWeekRecipeList.add(favouriteWeekRecipeList.get(x).copy());
        }
        return copyList;
    }
}
