package com.example.week_recipe.dao.converter;

import androidx.room.TypeConverter;

import com.example.week_recipe.model.domain.food.FoodList;
import com.google.gson.Gson;

public class FoodListConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static FoodList stringToFoodList(String json)
    {
        return gson.fromJson(json,FoodList.class);
    }

    @TypeConverter
    public static String foodListToString(FoodList foodList)
    {
        return gson.toJson(foodList);
    }
}
