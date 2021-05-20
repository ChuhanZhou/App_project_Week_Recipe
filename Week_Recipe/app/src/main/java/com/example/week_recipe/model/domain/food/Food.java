package com.example.week_recipe.model.domain.food;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.week_recipe.R;
import com.example.week_recipe.utility.MyPicture;

import java.time.LocalDateTime;

@Entity(tableName = "food_table")
public class Food {
    private String name;
    private FoodType type;
    private IngredientsList ingredientsList;
    private String imageId;

    public static int getStringIdByFoodType(FoodType type)
    {
        switch (type)
        {
            case Meat:
                return R.string.foodType_meat;
            case Vegetable:
                return R.string.foodType_vegetable;
            case Cereals:
                return R.string.foodType_cereals;
            case Beans:
                return R.string.foodType_beans;
            case Eggs:
                return R.string.foodType_eggs;
            case DairyProducts:
                return R.string.foodType_dairyProducts;
            case AquaticProducts:
                return R.string.foodType_aquaticProducts;
            case Fruit:
                return R.string.foodType_fruit;
            case Other:
                return R.string.foodType_other;
            default:
                return R.string.foodType_other;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public static FoodType getFoodTypeByStringId(int id)
    {
        switch (id)
        {
            case R.string.foodType_meat:
                return FoodType.Meat;
            case R.string.foodType_vegetable:
                return FoodType.Vegetable;
            case R.string.foodType_cereals:
                return FoodType.Cereals;
            case R.string.foodType_beans:
                return FoodType.Beans;
            case R.string.foodType_eggs:
                return FoodType.Eggs;
            case R.string.foodType_dairyProducts:
                return FoodType.DairyProducts;
            case R.string.foodType_aquaticProducts:
                return FoodType.AquaticProducts;
            case R.string.foodType_fruit:
                return FoodType.Fruit;
            case R.string.foodType_other:
                return FoodType.Other;
            default:
                return FoodType.Other;
        }
    }

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
        //id = email + "_" + name;
        //userEmail = email;
        this.name = name;
        this.type = type;
        this.ingredientsList = new IngredientsList();
        if (ingredientsList!=null)
        {
            this.ingredientsList = ingredientsList;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(Food newFood)
    {
        if (MyPicture.hasImage(newFood.imageId))
        {
            MyPicture.putBitmapByImageId(imageId,MyPicture.getBitmapByImageId(newFood.imageId));
        }
        name = newFood.name;
        type = newFood.type;
        ingredientsList = newFood.ingredientsList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean hasImage()
    {
        return MyPicture.hasImage(imageId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
