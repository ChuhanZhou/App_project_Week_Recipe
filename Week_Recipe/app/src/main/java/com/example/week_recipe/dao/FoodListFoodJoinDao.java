package com.example.week_recipe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.week_recipe.dao.join.FoodListFoodJoin;
import com.example.week_recipe.model.domain.food.Food;

import java.util.List;

@Dao
public interface FoodListFoodJoinDao {
    //@Insert
    //void insert(FoodListFoodJoin foodListFoodJoin);
//
    //@Query("SELECT * FROM food_table " +
    //        "JOIN foodList_food_join " +
    //        "ON food_table.id=foodList_food_join.foodId " +
    //        "WHERE foodList_food_join.foodListId=:foodListId")
    //List<Food> getFoodByFoodListId(int foodListId);
}
