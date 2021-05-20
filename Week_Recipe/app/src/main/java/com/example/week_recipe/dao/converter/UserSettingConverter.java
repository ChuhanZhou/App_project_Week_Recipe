package com.example.week_recipe.dao.converter;

import androidx.room.TypeConverter;

import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.model.domain.user.UserSetting;
import com.google.gson.Gson;

public class UserSettingConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static UserSetting stringToUserSetting(String json)
    {
        return gson.fromJson(json,UserSetting.class);
    }

    @TypeConverter
    public static String userSettingToString(UserSetting userSetting)
    {
        return gson.toJson(userSetting);
    }
}
