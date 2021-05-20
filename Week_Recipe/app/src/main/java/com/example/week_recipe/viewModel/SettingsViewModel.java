package com.example.week_recipe.viewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;

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

    public void updateUserName(String userName)
    {
        systemModel.updateUserName(userName);
    }
}
