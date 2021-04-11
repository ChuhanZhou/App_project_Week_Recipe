package com.example.week_recipe.model.domain.recipe;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.model.domain.food.FoodList;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecipeList {

    private ArrayList<DailyRecipe> recipeList;

    public RecipeList()
    {
        recipeList = new ArrayList<>();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean hasDailyRecipe(DailyRecipe dailyRecipe)
    {
        if (dailyRecipe.getDate()!=null)
        {
            return getByDate(dailyRecipe.getDate())!=null;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String add(DailyRecipe newDailyRecipe)
    {
        if (newDailyRecipe!=null)
        {
            if (!hasDailyRecipe(newDailyRecipe))
            {
                recipeList.add(newDailyRecipe);
            }
            return "This date has a recipe";
        }
        return "Can't input null";
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public DailyRecipe getByDate(LocalDate date)
    {
        if (date!=null)
        {
            for (int x=0;x<recipeList.size();x++)
            {
                if (recipeList.get(x).getDate().equals(date))
                {
                    return recipeList.get(x);
                }
            }
        }
        return null;
    }

    public DailyRecipe getByIndex(int index)
    {
        if (index>=0&&index<recipeList.size())
        {
            return recipeList.get(index);
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public RecipeList getOneWeek(LocalDate dateOfWeek)
    {
        LocalDate date = dateOfWeek.minusDays(dateOfWeek.getDayOfWeek().getValue());
        RecipeList weekRecipe = new RecipeList();
        for (int x=0;x<7;x++)
        {
            weekRecipe.add(getByDate(date));
            date.plusDays(1);
        }
        return weekRecipe;
    }

    //Non-duplicate food
    public FoodList getFoodMenu()
    {
        FoodList menu = new FoodList();
        for (int x=0;x<recipeList.size();x++)
        {
            FoodList dailyMenu = recipeList.get(x).getFoodMenu();
            for (int i=0;i<dailyMenu.getSize();i++)
            {
                menu.add(dailyMenu.getByIndex(i));
            }
        }
        return menu;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String update(DailyRecipe dailyRecipe)
    {
        if (dailyRecipe.getDate()!=null)
        {
            for (int x=0;x<recipeList.size();x++)
            {
                if (recipeList.get(x).getDate().equals(dailyRecipe.getDate()))
                {
                    recipeList.set(x,dailyRecipe);
                    return null;
                }
            }
            return "Can't find the recipe of " + dailyRecipe.getDate().toString();
        }
        return "The date can't be null.";
    }

    public String update(int index,DailyRecipe dailyRecipe)
    {
        if (getByIndex(index)!=null)
        {
            recipeList.set(index,dailyRecipe);
            return null;
        }
        return "Can't find the recipe in index " + index;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void remove(DailyRecipe dailyRecipe)
    {
        if (dailyRecipe.getDate()!=null)
        {
            recipeList.remove(getByDate(dailyRecipe.getDate()));
        }
    }

    public void remove(int index)
    {
        recipeList.remove(index);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RecipeList copy()
    {
        RecipeList copy = new RecipeList();
        for (int x=0;x<recipeList.size();x++)
        {
            copy.add(recipeList.get(x).copy());
        }
        return copy;
    }

    public int getSize()
    {
        return recipeList.size();
    }
}
