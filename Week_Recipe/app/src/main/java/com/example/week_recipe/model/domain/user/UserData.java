package com.example.week_recipe.model.domain.user;

import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;

import java.util.ArrayList;

public class UserData {
    private String email;
    private String userName;
    private RecipeList myDailyRecipeList;
    private ArrayList<RecipeList> favoriteWeekRecipeList;
    private FoodList favoriteFoodList;

    public UserData(String email,String userName)
    {
        this.email = email;
        this.userName = userName;
        myDailyRecipeList = new RecipeList();
        favoriteWeekRecipeList = new ArrayList<>();
        favoriteFoodList = new FoodList();
    }

    private RecipeList removeAllDate(RecipeList recipeList)
    {
        for (int x=0;x<recipeList.getSize();x++)
        {
            recipeList.getByIndex(x).setDate(null);
        }
        return recipeList;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public RecipeList getMyDailyRecipeList() {
        return myDailyRecipeList;
    }

    public FoodList getFavoriteFoodList() {
        return favoriteFoodList;
    }

    public ArrayList<RecipeList> getFavoriteWeekRecipeList() {
        return favoriteWeekRecipeList;
    }

    public FoodList getAllFood()
    {
        FoodList allFood = new FoodList();
        for (int x=0;x<favoriteFoodList.getSize();x++)
        {
            allFood.add(favoriteFoodList.getByIndex(x));
        }
        for (int x=0;x<myDailyRecipeList.getFoodMenu().getSize();x++)
        {
            allFood.add(myDailyRecipeList.getFoodMenu().getByIndex(x));
        }
        for (int x=0;x<favoriteWeekRecipeList.size();x++)
        {
            for (int i=0;i<favoriteWeekRecipeList.get(x).getFoodMenu().getSize();i++)
            {
                allFood.add(favoriteWeekRecipeList.get(x).getFoodMenu().getByIndex(x));
            }
        }
        return allFood;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String addFavoriteWeekRecipe(RecipeList newRecipeList)
    {
        if (newRecipeList!=null)
        {
            if (newRecipeList.getSize()<=7)
            {
                favoriteWeekRecipeList.add(removeAllDate(newRecipeList));
            }
            return "The daily recipe should less than 8.";
        }
        return "Input null!";
    }

    public RecipeList getFavoriteWeekRecipeByIndex(int index)
    {
        if (index>=0&&index<favoriteWeekRecipeList.size())
        {
            return favoriteWeekRecipeList.get(index);
        }
        return null;
    }

    public String updateFavoriteWeekRecipe(RecipeList oldRecipeList,RecipeList newRecipeList)
    {
        if (oldRecipeList!=null&&newRecipeList!=null)
        {
            for (int x=0;x<favoriteWeekRecipeList.size();x++)
            {
                if (favoriteWeekRecipeList.get(x).hashCode()==oldRecipeList.hashCode())
                {
                    if (newRecipeList.getSize()<=7)
                    {
                        favoriteWeekRecipeList.set(x,removeAllDate(newRecipeList));
                        return null;
                    }
                    return "The daily recipe should less than 8.";
                }
            }
            return "Can't find old Recipe List.";
        }
        return "Input null!";
    }

    public void removeFavoriteWeekRecipeByWeekRecipe(RecipeList recipeList)
    {
        for (int x=0;x<favoriteWeekRecipeList.size();x++)
        {
            if (favoriteWeekRecipeList.get(x).hashCode()==recipeList.hashCode())
            {
                favoriteWeekRecipeList.remove(x);
            }
        }
    }

    public void removeFavoriteWeekRecipeByIndex(int index)
    {
        favoriteWeekRecipeList.remove(index);
    }

    public UserData copy()
    {
        UserData copy = new UserData(email,userName);
        copy.myDailyRecipeList = myDailyRecipeList.copy();
        for (int x=0;x<favoriteWeekRecipeList.size();x++)
        {
            copy.favoriteWeekRecipeList.add(favoriteWeekRecipeList.get(x).copy());
        }
        copy.favoriteFoodList = favoriteFoodList.copy();
        return copy;
    }
}
