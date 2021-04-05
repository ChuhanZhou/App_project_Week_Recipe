package com.example.week_recipe.model.domain.food;

import android.graphics.Bitmap;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Food {
    private String name;
    private FoodType type;
    private IngredientsList ingredientsList;
    private Bitmap image;

    public Food(String name,FoodType type,IngredientsList ingredientsList,Bitmap image)
    {
        this.name = name;
        this.type = type;
        this.ingredientsList = ingredientsList;
        if (image!=null)
        {
            this.image = image;
        }
    }

    public Food(String name,FoodType type,IngredientsList ingredientsList)
    {
        this.name = name;
        this.type = type;
        this.ingredientsList = ingredientsList;
        image = null;
    }

    public void update(Food newFood)
    {
        image = newFood.image;
        name = newFood.name;
        type = newFood.type;
        ingredientsList = newFood.ingredientsList;
    }

    public boolean hasImage()
    {
        return image!=null;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public FoodType getType() {
        return type;
    }

    public IngredientsList getIngredientsList() {
        return ingredientsList;
    }

    public Food copy()
    {
        return new Food(name,type,ingredientsList.copy(),image);
    }
}
