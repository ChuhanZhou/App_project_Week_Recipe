package com.example.week_recipe.model.domain.recipe;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class FavouriteWeekRecipe{
    private String name;
    private RecipeList recipeList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FavouriteWeekRecipe(String name, RecipeList recipeList)
    {
        this.name = name;
        this.recipeList = new RecipeList();
        recipeList = recipeList.copy();
        for (int x=0;x<7;x++)
        {
            if (x<recipeList.getSize())
            {
                recipeList.getByIndex(x).setDate(null);
                this.recipeList.add(recipeList.getByIndex(x));
            }
            else
            {
                this.recipeList.add(new DailyRecipe());
            }
        }
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfFood()
    {
        int num = 0;
        for (int x=0;x<recipeList.getSize();x++)
        {
            num+=recipeList.getByIndex(x).getBreakfast().getSize()+recipeList.getByIndex(x).getLunch().getSize()+recipeList.getByIndex(x).getDinner().getSize();
        }
        return num;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FavouriteWeekRecipe copy() {
        return new FavouriteWeekRecipe(name,recipeList.copy());
    }
}
