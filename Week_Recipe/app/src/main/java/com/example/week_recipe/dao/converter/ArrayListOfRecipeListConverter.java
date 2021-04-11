package com.example.week_recipe.dao.converter;

import androidx.room.TypeConverter;

import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ArrayListOfRecipeListConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<RecipeList> stringToArrayListOfRecipeList(String json)
    {
        return gson.fromJson(json,ArrayList.class);
    }

    @TypeConverter
    public static String arrayListOfRecipeListToString(ArrayList<RecipeList> arrayListOfRecipeList)
    {
        return gson.toJson(arrayListOfRecipeList);
    }
}
