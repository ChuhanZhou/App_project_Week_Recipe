package com.example.week_recipe.viewModel;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.user.UserData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HomePageViewModel extends AndroidViewModel implements PropertyChangeListener {
    private final SystemModel systemModel;
    private final MutableLiveData<UserData> userData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HomePageViewModel(Application application)
    {
        super(application);
        systemModel = SystemModelManager.getSystemModelManager();
        userData = new MutableLiveData<>();
        userData.setValue(systemModel.getUserData());
        systemModel.addListener("setUserData",this);
        systemModel.addListener("updateUserBasicInformation",this);
    }

    public LiveData<UserData> getUserData() {
        return userData;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName())
        {
            case "setUserData":
            case "updateUserBasicInformation":
                userData.postValue(systemModel.getUserData());
                break;
        }
    }
}
