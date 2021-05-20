package com.example.week_recipe.viewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.model.domain.user.UserSetting;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SettingsViewModel extends ViewModel {
    private final SystemModel systemModel;

    public SettingsViewModel()
    {
        systemModel = SystemModelManager.getSystemModelManager();
    }

    public String getUserName()
    {
        return systemModel.getUserData().getUserName();
    }

    public UserSetting getUserSetting()
    {
        return systemModel.getUserData().getSetting();
    }

    public void updateUserName(String userName)
    {
        systemModel.updateUserName(userName);
    }

    public void updateUserSetting(UserSetting userSetting)
    {
        systemModel.updateUserSetting(userSetting);
    }

    public void initSystemModel()
    {
        systemModel.setUserData(new UserData("null","null"));
    }
}
