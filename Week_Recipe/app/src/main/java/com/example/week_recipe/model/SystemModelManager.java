package com.example.week_recipe.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.model.domain.user.UserData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;

public class SystemModelManager implements SystemModel{
    private static SystemModelManager systemModelManager;
    private PropertyChangeSupport property;
    private UserData userData;

    private SystemModelManager()
    {
        //this.account = null;
        userData = null;
        property = new PropertyChangeSupport(this);
    }

    public static SystemModelManager getSystemModelManager() {
        if (systemModelManager==null)
        {
            systemModelManager = new SystemModelManager();
        }
        return systemModelManager;
    }

    @Override
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public UserData getUserData() {
        return userData.copy();
    }

    @Override
    public String addDailyRecipe(DailyRecipe dailyRecipe) {
        String result = userData.getMyDailyRecipeList().add(dailyRecipe);
        if (result==null)
        {
            property.firePropertyChange("updateDailyRecipeList",null,dailyRecipe);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String addDailyRecipeList(RecipeList recipeList, LocalDate dateOfWeek) {
        LocalDate date = dateOfWeek.minusDays(dateOfWeek.getDayOfWeek().getValue());
        if (recipeList.getSize()<=7)
        {
            for (int x=0;x<7;x++)
            {
                DailyRecipe dailyRecipe;
                if (recipeList.getSize()>x)
                {
                    dailyRecipe = recipeList.getByIndex(x);
                    dailyRecipe.setDate(date);
                }
                else
                {
                    dailyRecipe = new DailyRecipe(date,new FoodList(),new FoodList(),new FoodList());
                }
                userData.getMyDailyRecipeList().add(dailyRecipe);
                property.firePropertyChange("updateDailyRecipeList",null,dailyRecipe);
            }
            return null;
        }
        return "The count of recipeList should less than 8.";
    }

    @Override
    public String updateDailyRecipe(DailyRecipe dailyRecipe) {
        if (dailyRecipe!=null)
        {
            if (dailyRecipe.getDate()!=null)
            {
                if (userData.getMyDailyRecipeList().hasDailyRecipe(dailyRecipe))
                {
                    userData.getMyDailyRecipeList().update(dailyRecipe);
                }
                else
                {
                    userData.getMyDailyRecipeList().add(dailyRecipe);
                }
                property.firePropertyChange("updateDailyRecipeList",null,dailyRecipe);
                return null;
            }
            return "The date can't be null!";
        }
        return "Input null!";
    }

    @Override
    public void removeDailyRecipe(DailyRecipe dailyRecipe) {
        userData.getMyDailyRecipeList().remove(dailyRecipe);
        property.firePropertyChange("updateDailyRecipeList",null,dailyRecipe);
    }

    @Override
    public String addFavoriteFood(Food favoriteFood) {
        return userData.getFavoriteFoodList().add(favoriteFood);
    }

    @Override
    public String updateFavoriteFood(Food oldFavoriteFood,Food newFavoriteFood) {
        return userData.getFavoriteFoodList().update(oldFavoriteFood, newFavoriteFood);
    }

    @Override
    public void removeFavoriteFood(Food favoriteFood) {
        userData.getFavoriteFoodList().remove(favoriteFood);
    }

    @Override
    public String addFavoriteWeekRecipe(RecipeList recipeList) {
        return userData.addFavoriteWeekRecipe(recipeList);
    }

    @Override
    public String updateFavoriteWeekRecipe(RecipeList oldRecipeList, RecipeList newRecipeList) {
        return userData.updateFavoriteWeekRecipe(oldRecipeList, newRecipeList);
    }

    @Override
    public void removeFavoriteWeekRecipe(RecipeList recipeList) {
        userData.removeFavoriteWeekRecipeByWeekRecipe(recipeList);
    }

    @Override
    public void removeFavoriteWeekRecipe(int index) {
        userData.removeFavoriteWeekRecipeByIndex(index);
    }

    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {
        property.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {
        property.removePropertyChangeListener(propertyName, listener);
    }
}
