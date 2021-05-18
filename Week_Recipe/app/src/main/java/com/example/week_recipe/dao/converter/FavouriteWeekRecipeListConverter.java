package com.example.week_recipe.dao.converter;

import androidx.room.TypeConverter;

import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipe;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FavouriteWeekRecipeListConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static FavouriteWeekRecipeList stringToFavouriteWeekRecipeList(String json)
    {
        return gson.fromJson(json,FavouriteWeekRecipeList.class);
    }

    @TypeConverter
    public static String favouriteWeekRecipeListToString(FavouriteWeekRecipeList favouriteWeekRecipe)
    {
        return gson.toJson(favouriteWeekRecipe);
    }
}
