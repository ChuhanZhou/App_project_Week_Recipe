package com.example.week_recipe.dao.converter;

import androidx.room.TypeConverter;

import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.google.gson.Gson;

public class RecipeListConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static RecipeList stringToRecipeList(String json)
    {
        return gson.fromJson(json,RecipeList.class);
    }

    @TypeConverter
    public static String recipeListToString(RecipeList recipeList)
    {
        return gson.toJson(recipeList);
    }
}
