package com.example.week_recipe.model.domain.user;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "account_table")
public class Account {
    @PrimaryKey
    @NonNull
    private String email;
    private String password;

    public Account(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkPassword(String inputPassword)
    {
        return password.equals(inputPassword);
    }

    public String changePassword(String oldPassword,String newPassword)
    {
        if (checkPassword(oldPassword))
        {
            if (newPassword!=null)
            {
                password = newPassword;
                return null;
            }
            return "The new password can't be null!";
        }
        return "The old password is wrong!";
    }
}
