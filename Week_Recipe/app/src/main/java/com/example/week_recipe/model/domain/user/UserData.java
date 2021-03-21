package com.example.week_recipe.model.domain.user;

import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;

import java.util.ArrayList;

public class UserData {
    private String email;
    private String userName;
    private RecipeList myDailyRecipeList;
    private ArrayList<RecipeList> favoriteWeekRecipe;
    private FoodList favoriteFoodList;

    public UserData(String email,String userName)
    {
        this.email = email;
        this.userName = userName;
        myDailyRecipeList = new RecipeList();
        favoriteWeekRecipe = new ArrayList<>();
        favoriteFoodList = new FoodList();
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserData copy()
    {
        UserData copy = new UserData(email,userName);
        return copy;
    }
}
