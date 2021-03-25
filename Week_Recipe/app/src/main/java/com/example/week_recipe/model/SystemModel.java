package com.example.week_recipe.model;

import com.example.week_recipe.model.domain.user.UserData;

public interface SystemModel {
    void setUserData(UserData userData);
    UserData getUserData();
}
