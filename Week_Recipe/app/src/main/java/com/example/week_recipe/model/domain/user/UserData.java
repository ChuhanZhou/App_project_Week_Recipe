package com.example.week_recipe.model.domain.user;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.week_recipe.dao.converter.ArrayListOfRecipeListConverter;
import com.example.week_recipe.dao.converter.FoodListConverter;
import com.example.week_recipe.dao.converter.LocalDateTimeConverter;
import com.example.week_recipe.dao.converter.RecipeListConverter;
import com.example.week_recipe.model.domain.food.FoodList;
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
    @TypeConverters(ArrayListOfRecipeListConverter.class)
    private ArrayList<RecipeList> favoriteWeekRecipeList;
    @TypeConverters(FoodListConverter.class)
    private FoodList favoriteFoodList;

    public UserData(String userImageId,String email,String userName,RecipeList myDailyRecipeList,ArrayList<RecipeList> favoriteWeekRecipeList,FoodList favoriteFoodList)
    {
        this.userImageId = userImageId;
        this.email = email;
        this.userName = userName;
        this.myDailyRecipeList = myDailyRecipeList;
        this.favoriteWeekRecipeList = favoriteWeekRecipeList;
        this.favoriteFoodList = favoriteFoodList;
        updateTime = LocalDateTimeConverter.localDateTimeToString(LocalDateTime.now());
    }
    @Ignore
    public UserData(String email,String userName)
    {
        userImageId = "userImage";
        this.email = email;
        this.userName = userName;
        myDailyRecipeList = new RecipeList();
        favoriteWeekRecipeList = new ArrayList<>();
        favoriteFoodList = new FoodList();
        updateTime = LocalDateTimeConverter.localDateTimeToString(LocalDateTime.now());
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

    public String updateFavoriteWeekRecipe(RecipeList oldRecipeList, RecipeList newRecipeList)
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
        ArrayList<RecipeList> copyList = new ArrayList<>();
        for (int x=0;x<favoriteWeekRecipeList.size();x++)
        {
            copyList.add(favoriteWeekRecipeList.get(x).copy());
        }
        return new UserData(userImageId,email,userName,myDailyRecipeList.copy(),copyList,favoriteFoodList.copy());
    }
}
