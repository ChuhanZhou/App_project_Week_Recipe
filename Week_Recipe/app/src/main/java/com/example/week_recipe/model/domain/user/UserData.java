package com.example.week_recipe.model.domain.user;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.week_recipe.dao.converter.FavouriteWeekRecipeListConverter;
import com.example.week_recipe.dao.converter.FoodListConverter;
import com.example.week_recipe.dao.converter.LocalDateTimeConverter;
import com.example.week_recipe.dao.converter.RecipeListConverter;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.utility.MyPicture;

import java.time.LocalDateTime;
import java.util.ArrayList;
@Entity(tableName = "userData_table")
@RequiresApi(api = Build.VERSION_CODES.O)
public class UserData {
    private String userImageId;
    @PrimaryKey
    @NonNull
    private String email;
    private String updateTime;
    private String userName;
    @TypeConverters(RecipeListConverter.class)
    private RecipeList myDailyRecipeList;
    @TypeConverters(FavouriteWeekRecipeListConverter.class)
    private FavouriteWeekRecipeList favoriteWeekRecipeList;
    @TypeConverters(FoodListConverter.class)
    private FoodList favoriteFoodList;

    public UserData(String userImageId,String email,String userName,RecipeList myDailyRecipeList,FavouriteWeekRecipeList favoriteWeekRecipeList,FoodList favoriteFoodList,String updateTime)
    {
        this.userImageId = userImageId;
        this.email = email;
        this.userName = userName;
        this.myDailyRecipeList = myDailyRecipeList;
        this.favoriteWeekRecipeList = favoriteWeekRecipeList;
        this.favoriteFoodList = favoriteFoodList;
        this.updateTime = updateTime;
    }
    @Ignore
    public UserData(String email,String userName)
    {
        userImageId = "_userImage";
        this.email = email;
        this.userName = userName;
        myDailyRecipeList = new RecipeList();
        favoriteWeekRecipeList = new FavouriteWeekRecipeList();
        favoriteFoodList = new FoodList();
        updateTime = "0.0.0 0:0:0";
    }

    public boolean hasUserImage()
    {
        return MyPicture.hasImage(userImageId);
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

    public FavouriteWeekRecipeList getFavoriteWeekRecipeList() {
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
        for (int x=0;x<favoriteWeekRecipeList.getSize();x++)
        {
            for (int i = 0; i<favoriteWeekRecipeList.getByIndex(x).getRecipeList().getFoodMenu().getSize(); i++)
            {
                allFood.add(favoriteWeekRecipeList.getByIndex(x).getRecipeList().getFoodMenu().getByIndex(i));
            }
        }
        return allFood;
    }

    public ArrayList<String> getAllImageId()
    {
        ArrayList<String> imageIdList = new ArrayList<>();
        FoodList allFood = getAllFood();
        for (int x=0;x<allFood.getSize();x++)
        {
            imageIdList.add(allFood.getByIndex(x).getImageId());
        }
        imageIdList.add(userImageId);
        return imageIdList;
    }

    public Bitmap getUserImage() {
        return MyPicture.getBitmapByImageId(userImageId);
    }

    @NonNull
    public String getUserImageId() {
        return userImageId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public LocalDateTime getUpdateTimeInLocalDate() {
        return LocalDateTimeConverter.stringToLocalDateTime(updateTime);
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void updateTime() {
        this.updateTime = LocalDateTimeConverter.localDateTimeToString(LocalDateTime.now());
    }

    public void setUserImage(Bitmap userImage) {
        MyPicture.putBitmapByImageId(userImageId,userImage);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserData copy()
    {
        return new UserData(userImageId,email,userName,myDailyRecipeList.copy(),favoriteWeekRecipeList.copy(),favoriteFoodList.copy(),updateTime);
    }
}
