package com.example.week_recipe.model;

import com.example.week_recipe.model.domain.user.UserData;

public class SystemModelManager implements SystemModel{
    private static SystemModelManager systemModelManager;
    private UserData userData;

    private SystemModelManager()
    {
        //this.account = null;
        userData = null;
    }

    public static SystemModelManager getSystemModelManager() {
        if (systemModelManager==null)
        {
            systemModelManager = new SystemModelManager();
        }
        return systemModelManager;
    }

    @Override
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public UserData getUserData() {
        return userData;
    }
}
