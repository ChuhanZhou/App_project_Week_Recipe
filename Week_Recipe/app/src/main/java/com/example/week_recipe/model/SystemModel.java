package com.example.week_recipe.model;

import android.graphics.Bitmap;

import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.model.domain.user.UserSetting;
import com.example.week_recipe.utility.NamedPropertyChangeSubject;

import java.time.LocalDate;

public interface SystemModel extends NamedPropertyChangeSubject {
    void setUserData(UserData userData);
    UserData getUserData();

    void updateUserName(String userName);
    void updateUserImage(Bitmap userImage);

    String addDailyRecipe(DailyRecipe dailyRecipe);
    String addDailyRecipeList(RecipeList recipeList, LocalDate dateOfWeek);
    String updateDailyRecipe(DailyRecipe dailyRecipe);
    void removeDailyRecipe(DailyRecipe dailyRecipe);

    String updateFoodOfAll(Food oldFood,Food newFood);

    String addFavoriteFood(Food favoriteFood);
    void removeFavoriteFood(Food favoriteFood);

    String addFavoriteWeekRecipe(String name,RecipeList recipeList);
    String updateFavoriteWeekRecipe(FavouriteWeekRecipe oldRecipeList, FavouriteWeekRecipe newRecipeList);
    void removeFavoriteWeekRecipe(String name);
    void removeFavoriteWeekRecipe(int index);

    String updateUserSetting(UserSetting setting);
}
