package com.example.week_recipe.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.week_recipe.dao.join.FoodListFoodJoin;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.user.Account;
import com.example.week_recipe.model.domain.user.UserData;

//@Database(entities = {Account.class, UserData.class,Food.class, FoodList.class,FoodListFoodJoin.class}, version = 1,exportSchema = false)
@Database(entities = {Account.class, UserData.class}, version = 1,exportSchema = false)
public abstract class SystemDatabase extends RoomDatabase {
    private static SystemDatabase instance;

    public abstract AccountDao accountDao();
    public abstract UserDataDao userDataDao();
    //public abstract FoodDao foodDao();
    //public abstract FoodListDao foodListDao();
    //public abstract FoodListFoodJoinDao foodListFoodJoinDao();

    public static synchronized SystemDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, SystemDatabase.class, "system_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
