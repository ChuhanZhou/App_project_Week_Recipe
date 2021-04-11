package com.example.week_recipe.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.week_recipe.model.domain.user.UserData;

@Dao
public interface UserDataDao {
    @Insert
    void insert(UserData userData);

    @Update
    void update(UserData userData);

    @Delete
    void delete(UserData userData);

    @Query("SELECT * FROM userData_table WHERE email = :email")
    UserData getUserDataByEmail(String email);

    @Query("DELETE FROM userData_table")
    void deleteAllUserData();
}
