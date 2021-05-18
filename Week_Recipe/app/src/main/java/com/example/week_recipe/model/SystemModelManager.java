package com.example.week_recipe.model;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.utility.MyPicture;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
@RequiresApi(api = Build.VERSION_CODES.O)
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
        MyPicture.loadAllBitmapToCache(userData.getAllImageId());
        //Food.setEmail(userData.getEmail());
    }

    @Override
    public UserData getUserData() {
        return userData.copy();
    }

    @Override
    public void updateUserName(String userName) {
        userData.setUserName(userName);
        property.firePropertyChange("updateUserBasicInformation",null,userData.copy());
    }

    @Override
    public void updateUserImage(Bitmap userImage) {
        userData.setUserImage(userImage);
        property.firePropertyChange("updateUserBasicInformation",null,userData.copy());
    }

    @Override
    public String addDailyRecipe(DailyRecipe dailyRecipe) {
        String result = userData.getMyDailyRecipeList().add(dailyRecipe);
        if (result==null)
        {
            userData.updateTime();
            property.firePropertyChange("updateDailyRecipeList",null,dailyRecipe);
        }
        return result;
    }

    @Override
    public String addDailyRecipeList(RecipeList recipeList, LocalDate dateOfWeek) {
        LocalDate date = dateOfWeek.minusDays(dateOfWeek.getDayOfWeek().getValue()-1);
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

                if (userData.getMyDailyRecipeList().hasDailyRecipe(dailyRecipe))
                {
                    userData.getMyDailyRecipeList().update(dailyRecipe);
                }
                else
                {
                    userData.getMyDailyRecipeList().add(dailyRecipe);
                }
                userData.updateTime();
                property.firePropertyChange("updateDailyRecipeList",null,dailyRecipe);
                date = date.plusDays(1);
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
                userData.updateTime();
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
        userData.updateTime();
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
        userData.updateTime();
        property.firePropertyChange("updateFood",oldFood,newFood);
        MyPicture.clearUselessBitmapInInternalStorage(userData.copy().getAllImageId());
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
        for (int i=0;i<userData.getFavoriteWeekRecipeList().getSize();i++)
        {
            RecipeList recipeList = userData.getFavoriteWeekRecipeList().getByIndex(i).getRecipeList();
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
            userData.updateTime();
            property.firePropertyChange("updateFavoriteFoodList",null,favoriteFood.copy());
        }
        return result;
    }

    @Override
    public void removeFavoriteFood(Food favoriteFood) {
        userData.getFavoriteFoodList().remove(favoriteFood);
        userData.updateTime();
        property.firePropertyChange("updateFavoriteFoodList",null,favoriteFood.copy());
    }

    @Override
    public String addFavoriteWeekRecipe(String name,RecipeList recipeList) {

        String result = userData.getFavoriteWeekRecipeList().add(name,recipeList);
        if (result==null)
        {
            userData.updateTime();
            property.firePropertyChange("updateFavoriteWeekRecipe",null,userData.copy().getFavoriteWeekRecipeList().getByName(name));
        }
        return result;
    }

    @Override
    public String updateFavoriteWeekRecipe(FavouriteWeekRecipe oldRecipeList, FavouriteWeekRecipe newRecipeList) {
        userData.updateTime();
        return userData.getFavoriteWeekRecipeList().update(oldRecipeList, newRecipeList);
    }

    @Override
    public void removeFavoriteWeekRecipe(String name) {
        userData.updateTime();
        userData.getFavoriteWeekRecipeList().removeByName(name);
    }

    @Override
    public void removeFavoriteWeekRecipe(int index) {
        userData.updateTime();
        userData.getFavoriteWeekRecipeList().removeByIndex(index);
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
