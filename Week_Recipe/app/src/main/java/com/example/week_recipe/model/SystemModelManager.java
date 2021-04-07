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
    public String updateFoodOfAll(Food oldFood, Food newFood) {
        String result = userData.copy().getAllFood().update(oldFood, newFood);
        if (result!=null)
        {
            return result;
        }
        result = updateFood(userData.copy(),oldFood,newFood);
        if (result!=null)
        {
            return result;
        }
        result = updateFood(userData,oldFood,newFood);
        property.firePropertyChange("updateFood",oldFood,newFood);
        return result;
    }

    private String updateFood(UserData userData,Food oldFood, Food newFood)
    {
        String initResult = "Can't find old food [" + oldFood.getName() + "]";
        String result = initResult;
        //update food in favourite food list
        if (userData.getFavoriteFoodList().hasFood(oldFood))
        {
            result = userData.getFavoriteFoodList().update(oldFood, newFood);
            if (result!=null)
            {
                return result;
            }
        }
        //update food in my daily recipe list
        for (int x=0;x<userData.getMyDailyRecipeList().getSize();x++)
        {
            //breakfast
            if (userData.getMyDailyRecipeList().getByIndex(x).getBreakfast().hasFood(oldFood))
            {
                result = userData.getMyDailyRecipeList().getByIndex(x).getBreakfast().update(oldFood, newFood);
            }
            if (result!=null&&!result.equals(initResult))
            {
                return result;
            }
            //lunch
            if (userData.getMyDailyRecipeList().getByIndex(x).getLunch().hasFood(oldFood))
            {
                result = userData.getMyDailyRecipeList().getByIndex(x).getLunch().update(oldFood, newFood);
            }
            if (result!=null&&!result.equals(initResult))
            {
                return result;
            }
            //dinner
            if (userData.getMyDailyRecipeList().getByIndex(x).getDinner().hasFood(oldFood))
            {
                result = userData.getMyDailyRecipeList().getByIndex(x).getDinner().update(oldFood, newFood);
            }
            if (result!=null&&!result.equals(initResult))
            {
                return result;
            }
        }
        //update food in favorite week recipe list
        for (int i=0;i<userData.getFavoriteWeekRecipeList().size();i++)
        {
            RecipeList recipeList = userData.getFavoriteWeekRecipeByIndex(i);
            for (int x=0;x<recipeList.getSize();x++)
            {
                //breakfast
                if (recipeList.getByIndex(x).getBreakfast().hasFood(oldFood))
                {
                    result = recipeList.getByIndex(x).getBreakfast().update(oldFood, newFood);
                }
                if (result!=null&&!result.equals(initResult))
                {
                    return result;
                }
                //lunch
                if (recipeList.getByIndex(x).getLunch().hasFood(oldFood))
                {
                    result = recipeList.getByIndex(x).getLunch().update(oldFood, newFood);
                }
                if (result!=null&&!result.equals(initResult))
                {
                    return result;
                }
                //dinner
                if (recipeList.getByIndex(x).getDinner().hasFood(oldFood))
                {
                    result = recipeList.getByIndex(x).getDinner().update(oldFood, newFood);
                }
                if (result!=null&&!result.equals(initResult))
                {
                    return result;
                }
            }
        }
        return result;
    }

    @Override
    public String addFavoriteFood(Food favoriteFood) {
        String result = userData.getFavoriteFoodList().add(favoriteFood);
        if (result == null)
        {
            property.firePropertyChange("updateFavoriteFoodList",null,favoriteFood);
        }
        return result;
    }

    @Override
    public void removeFavoriteFood(Food favoriteFood) {
        userData.getFavoriteFoodList().remove(favoriteFood);
        property.firePropertyChange("updateFavoriteFoodList",null,favoriteFood);
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
