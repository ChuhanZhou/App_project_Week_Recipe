package com.example.week_recipe.viewModel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RecipeForWeekViewModel extends AndroidViewModel implements PropertyChangeListener {
    private final SystemModel systemModel;
    private LocalDate firstDayOfWeek;
    private final MutableLiveData<String> showWeekText;
    private final MutableLiveData<RecipeList> showRecipeList;

    public RecipeForWeekViewModel(Application application)
    {
        super(application);
        //init
        systemModel = SystemModelManager.getSystemModelManager();
        showWeekText = new MutableLiveData<>();
        showRecipeList = new MutableLiveData<>();
        //set value
        setShowDate(LocalDate.now());
        //add listener

        systemModel.addListener("setUserData",this);
        systemModel.addListener("updateDailyRecipeList",this);
        systemModel.addListener("updateFood",this);
    }

    public LiveData<String> getShowWeekText() {
        return showWeekText;
    }

    public LocalDate getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public LiveData<RecipeList> getShowRecipeList() {
        return showRecipeList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setShowDate(LocalDate dayOfWeek) {
        LocalDate firstDayOfWeek = dayOfWeek.minusDays(dayOfWeek.getDayOfWeek().getValue()-1);
        if (this.firstDayOfWeek!=firstDayOfWeek)
        {
            this.firstDayOfWeek = firstDayOfWeek;
            showWeekText.setValue(this.firstDayOfWeek.getMonthValue()+"/"+this.firstDayOfWeek.getDayOfMonth()+"-"+this.firstDayOfWeek.plusDays(6).getMonthValue()+"/"+this.firstDayOfWeek.plusDays(6).getDayOfMonth());
            RecipeList recipeList = systemModel.getUserData().getMyDailyRecipeList().getOneWeek(this.firstDayOfWeek);
            showRecipeList.setValue(recipeList);
        }
    }

    public String addFavouriteWeekRecipe(String name)
    {
        return systemModel.addFavoriteWeekRecipe(name,showRecipeList.getValue());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName())
        {
            case "updateDailyRecipeList":
                DailyRecipe update = (DailyRecipe) evt.getNewValue();
                if (update.getDate().minusDays(update.getDate().getDayOfWeek().getValue()-1).equals(firstDayOfWeek))
                {
                    showRecipeList.setValue(systemModel.getUserData().getMyDailyRecipeList().getOneWeek(firstDayOfWeek));
                }
                break;
            case "setUserData":
            case "updateFood":
                showRecipeList.postValue(systemModel.getUserData().getMyDailyRecipeList().getOneWeek(firstDayOfWeek));
                //favouriteFoodList.setValue(systemModel.getUserData().getFavoriteFoodList());
                break;
        }
    }
}
