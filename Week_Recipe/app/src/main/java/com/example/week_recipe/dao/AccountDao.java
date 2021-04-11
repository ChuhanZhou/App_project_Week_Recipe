package com.example.week_recipe.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.week_recipe.model.domain.user.Account;
import com.example.week_recipe.model.domain.user.UserData;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert
    void insert(Account account);

    @Update
    void update(Account account);

    @Delete
    void delete(Account account);

    @Query("SELECT * FROM account_table")
    List<Account> getAllAccount();

    @Query("DELETE FROM account_table")
    void deleteAllAccount();
}
