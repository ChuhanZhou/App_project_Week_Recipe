package com.example.week_recipe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FoodDao {
    //@Insert
    //void insert(Food food);
//
    //@Query("DELETE FROM food_table")
    //void deleteAllFood();
//
    //@Query("SELECT * FROM food_table WHERE id = :id")
    //Food getFoodByName(String id);
//
    //@Query("SELECT * FROM food_table ORDER BY name DESC")
    //List<Food> getAllFood();
//
    //@Query("SELECT * FROM food_table WHERE userEmail = :userEmail")
    //List<Food> getFoodByEmail(String userEmail);
//
    //@Query("UPDATE food_table SET id = :newId,userEmail = :userEmail,name = :newName,type=:newType,ingredientsList=:newIngredientsList,imageId=:imageId WHERE id=:oldId")
    //void update(String oldId, String newId, String userEmail,String newName, String newType, String newIngredientsList,String imageId);
//
    //@Delete
    //void delete(Food food);
}
