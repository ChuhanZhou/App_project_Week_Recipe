package com.example.week_recipe.dao.join;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;

@Entity(tableName = "foodList_food_join",
        primaryKeys = {"foodListId", "foodId"},
        foreignKeys = {
                @ForeignKey(entity = FoodList.class,
                        parentColumns = "id",
                        childColumns = "foodListId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Food.class,
                        parentColumns = "id",
                        childColumns = "foodId", onDelete = ForeignKey.CASCADE)}, indices = {@Index(value = "foodId"), @Index(value = "foodListId")
})
public class FoodListFoodJoin {

    public int foodListId;
    @NonNull
    public String foodId;
}
