package com.example.week_recipe.dao.converter;

import androidx.room.TypeConverter;

import com.example.week_recipe.model.domain.food.FoodType;

public class FoodTypeConverter {
    @TypeConverter
    public static FoodType stringToFoodType(String foodType)
    {
        return FoodType.valueOf(foodType);
    }

    @TypeConverter
    public static String FoodTypeToString(FoodType foodType)
    {
        return foodType.toString();
    }
}
