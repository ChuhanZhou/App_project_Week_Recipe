package com.example.week_recipe.model.domain.food;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.week_recipe.dao.converter.FoodTypeConverter;
import com.example.week_recipe.dao.converter.IngredientsListConverter;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.utility.MyPicture;

import java.time.LocalDateTime;

@Entity(tableName = "food_table")
public class Food {
    //private static String email;
    //@PrimaryKey
    //@NonNull
    //private String id;
    //private String userEmail;
    private String name;
    //@TypeConverters(FoodTypeConverter.class)
    private FoodType type;
    //@TypeConverters(IngredientsListConverter.class)
    private IngredientsList ingredientsList;
    private String imageId;

    //public static void setEmail(String email) {
    //    Food.email = email;
    //}
//
    //public static String getEmail() {
    //    return email;
    //}

    @Ignore
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
        //this.id = id;
        //this.userEmail = userEmail;
        this.imageId = imageId;
        this.name = name;
        this.type = type;
        this.ingredientsList = new IngredientsList();
        if (ingredientsList!=null)
        {
            this.ingredientsList = ingredientsList;
        }
    }
    @Ignore
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
        MyPicture.putBitmapByImageId(imageId,MyPicture.getBitmapByImageId(newFood.imageId));
        //id = email+"_"+newFood.name;
        //userEmail = newFood.userEmail;
        name = newFood.name;
        type = newFood.type;
        ingredientsList = newFood.ingredientsList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean hasImage()
    {
        return MyPicture.hasImage(imageId);
    }

    //@NonNull
    //public String getId() {
    //    return id;
    //}
//
    //public String getUserEmail() {
    //    return userEmail;
    //}

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
