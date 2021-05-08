package com.example.week_recipe.model.domain.recipe;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.dao.converter.LocalDateConverter;
import com.example.week_recipe.model.domain.food.FoodList;

import java.time.LocalDate;

//@Entity(tableName = "dailyRecipe_table")

public class DailyRecipe {
    //@PrimaryKey(autoGenerate = true)
    //private int id;
    //@TypeConverters(LocalDateConverter.class)
    private String date;
    //@Embedded
    private FoodList breakfast;
    //@Embedded
    private FoodList lunch;
    //@Embedded
    private FoodList dinner;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DailyRecipe(LocalDate date, FoodList breakfast, FoodList lunch, FoodList dinner) {
        this(date);
        if (breakfast != null) {
            this.breakfast = breakfast;
        }
        if (lunch != null) {
            this.lunch = lunch;
        }
        if (dinner != null) {
            this.dinner = dinner;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DailyRecipe(LocalDate date)
    {
        this.date = LocalDateConverter.localDateToString(date);
        this.breakfast = new FoodList();
        this.lunch = new FoodList();
        this.dinner = new FoodList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DailyRecipe()
    {
        this(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getDate() {
        return LocalDateConverter.stringToLocalDate(date);
    }

    public FoodList getBreakfast() {
        return breakfast;
    }

    public FoodList getLunch() {
        return lunch;
    }

    public FoodList getDinner() {
        return dinner;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(LocalDate date) {
        this.date = LocalDateConverter.localDateToString(date);
    }

    public void setBreakfast(FoodList breakfast) {
        this.breakfast = breakfast;
    }

    public void setLunch(FoodList lunch) {
        this.lunch = lunch;
    }

    public void setDinner(FoodList dinner) {
        this.dinner = dinner;
    }

    //Non-duplicate food
    public FoodList getFoodMenu()
    {
        FoodList menu = new FoodList();
        FoodList[] listOfFoodList = {breakfast,lunch,dinner};
        for (int x=0;x<listOfFoodList.length;x++)
        {
            for (int i=0;i<listOfFoodList[x].getSize();i++)
            {
                menu.add(listOfFoodList[x].getByIndex(i));
            }
        }
        return menu;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DailyRecipe copy() {
        return new DailyRecipe(LocalDateConverter.stringToLocalDate(date), breakfast.copy(), lunch.copy(), dinner.copy());
    }
}
