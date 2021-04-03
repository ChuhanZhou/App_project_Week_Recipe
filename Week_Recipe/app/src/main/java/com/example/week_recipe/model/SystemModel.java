package com.example.week_recipe.model;

import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.utility.NamedPropertyChangeSubject;

import java.time.LocalDate;

public interface SystemModel extends NamedPropertyChangeSubject {
    void setUserData(UserData userData);
    UserData getUserData();

    String addDailyRecipe(DailyRecipe dailyRecipe);
    String addDailyRecipeList(RecipeList recipeList, LocalDate dateOfWeek);
    String updateDailyRecipe(DailyRecipe dailyRecipe);
    void removeDailyRecipe(DailyRecipe dailyRecipe);

    String updateFoodOfAll(Food oldFood,Food newFood);

    String addFavoriteFood(Food favoriteFood);
    void removeFavoriteFood(Food favoriteFood);

    String addFavoriteWeekRecipe(RecipeList recipeList);
    String updateFavoriteWeekRecipe(RecipeList oldRecipeList,RecipeList newRecipeList);
    void removeFavoriteWeekRecipe(RecipeList recipeList);
    void removeFavoriteWeekRecipe(int index);
}
