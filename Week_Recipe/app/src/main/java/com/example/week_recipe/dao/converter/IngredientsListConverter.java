package com.example.week_recipe.dao.converter;

import androidx.room.TypeConverter;

import com.example.week_recipe.model.domain.food.IngredientsList;
import com.google.gson.Gson;

public class IngredientsListConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static IngredientsList stringToIngredientsList(String json)
    {
        return gson.fromJson(json,IngredientsList.class);
    }

    @TypeConverter
    public static String ingredientsListToString(IngredientsList ingredientsList)
    {
        return gson.toJson(ingredientsList);
    }
}
