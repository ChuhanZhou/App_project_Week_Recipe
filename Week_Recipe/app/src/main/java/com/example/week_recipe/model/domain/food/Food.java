package com.example.week_recipe.model.domain.food;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.utility.MyPicture;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Food {
    private String name;
    private FoodType type;
    private IngredientsList ingredientsList;
    private String imageId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Food(String name, FoodType type, IngredientsList ingredientsList, Bitmap image)
    {
        this(name, type, ingredientsList);
        if (image!=null)
        {
            MyPicture.putBitmapByImageId(imageId,image);
        }
    }

    public Food(String name, FoodType type, IngredientsList ingredientsList,String imageId)
    {
        this.imageId = imageId;
        this.name = name;
        this.type = type;
        this.ingredientsList = new IngredientsList();
        if (ingredientsList!=null)
        {
            this.ingredientsList = ingredientsList;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Food(String name, FoodType type, IngredientsList ingredientsList)
    {
        imageId = "_foodImage_"+name.hashCode()+"_"+ LocalDateTime.now().hashCode();
        this.name = name;
        this.type = type;
        this.ingredientsList = new IngredientsList();
        if (ingredientsList!=null)
        {
            this.ingredientsList = ingredientsList;
        }
    }

    public void update(Food newFood)
    {
        imageId = newFood.imageId;
        name = newFood.name;
        type = newFood.type;
        ingredientsList = newFood.ingredientsList;
    }

    public boolean hasImage()
    {
        return MyPicture.hasImage(imageId);
    }

    public Bitmap getImage() {
        return MyPicture.getBitmapByImageId(imageId);
    }

    public String getImageId() {
        return imageId;
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
        return new Food(name,type,ingredientsList.copy(),imageId);
    }
}
