package com.example.week_recipe.ViewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.utility.NamedPropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class RecipeWithDateViewModel extends ViewModel implements PropertyChangeListener {

    private SystemModel systemModel;
    private LocalDate showDate;
    private MutableLiveData<String> showDateText;
    private MutableLiveData<DailyRecipe> showRecipe;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RecipeWithDateViewModel()
    {
        systemModel = SystemModelManager.getSystemModelManager();
        showDateText = new MutableLiveData<>();
        showRecipe = new MutableLiveData<>();
        setShowDate(LocalDate.now());
        systemModel.addListener("updateDailyRecipeList",this);
    }

    public LiveData<String> getShowDateText() {
        return showDateText;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public LiveData<DailyRecipe> getShowRecipe() {
        return showRecipe;
    }

    public void deleteFood(int listIndex,int itemIndex)
    {
        DailyRecipe dailyRecipe = systemModel.getUserData().getMyDailyRecipeList().getByDate(showDate);
        switch (listIndex)
        {
            case 0:
                dailyRecipe.getBreakfast().remove(itemIndex);
                break;
            case 1:
                dailyRecipe.getLunch().remove(itemIndex);
                break;
            case 2:
                dailyRecipe.getDinner().remove(itemIndex);
                break;
        }
        systemModel.updateDailyRecipe(dailyRecipe);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
        showDateText.setValue(this.showDate.getMonthValue()+"/"+this.showDate.getDayOfMonth());
        showRecipe.setValue(systemModel.getUserData().getMyDailyRecipeList().getByDate(showDate));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName())
        {
            case "updateDailyRecipeList":
                if (((DailyRecipe) evt.getNewValue()).getDate().equals(showDate))
                {
                    showRecipe.setValue(systemModel.getUserData().getMyDailyRecipeList().getByDate(showDate));
                }
                break;
        }
    }
}
