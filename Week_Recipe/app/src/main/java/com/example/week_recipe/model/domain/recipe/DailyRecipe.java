package com.example.week_recipe.model.domain.recipe;

import com.example.week_recipe.model.domain.food.FoodList;

import java.time.LocalDate;
import java.util.Date;

public class DailyRecipe {
    private LocalDate date;
    private FoodList breakfast;
    private FoodList lunch;
    private FoodList dinner;

    public DailyRecipe(LocalDate date, FoodList breakfast, FoodList lunch, FoodList dinner) {
        this.date = date;
        this.breakfast = new FoodList();
        this.lunch = new FoodList();
        this.dinner = new FoodList();
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

    public LocalDate getDate() {
        return date;
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

    public void setDate(LocalDate date) {
        this.date = date;
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

    public DailyRecipe copy() {
        return new DailyRecipe(date, breakfast.copy(), lunch.copy(), dinner.copy());
    }
}
