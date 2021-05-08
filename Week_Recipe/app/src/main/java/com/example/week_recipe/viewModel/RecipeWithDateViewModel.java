package com.example.week_recipe.viewModel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RecipeWithDateViewModel extends AndroidViewModel implements PropertyChangeListener {

    private final SystemModel systemModel;
    private LocalDate showDate;
    private final MutableLiveData<String> showDateText;
    private final MutableLiveData<DailyRecipe> showRecipe;
    private final MutableLiveData<FoodList> favouriteFoodList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RecipeWithDateViewModel(Application application)
    {
        super(application);
        //init
        systemModel = SystemModelManager.getSystemModelManager();
        showDateText = new MutableLiveData<>();
        showRecipe = new MutableLiveData<>();
        favouriteFoodList = new MutableLiveData<>();
        //set value
        setShowDate(LocalDate.now());
        favouriteFoodList.setValue(systemModel.getUserData().getFavoriteFoodList());
        //add listener
        systemModel.addListener("updateDailyRecipeList",this);
        systemModel.addListener("updateFavoriteFoodList",this);
        systemModel.addListener("updateFood",this);
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

    public LiveData<FoodList> getFavouriteFoodList() {
        return favouriteFoodList;
    }

    public void changeLikeState(int listIndex, int itemIndex)
    {
        DailyRecipe dailyRecipe = systemModel.getUserData().getMyDailyRecipeList().getByDate(showDate);
        Food food = null;
        switch (listIndex)
        {
            case 0:
                food = dailyRecipe.getBreakfast().getByIndex(itemIndex);
                break;
            case 1:
                food = dailyRecipe.getLunch().getByIndex(itemIndex);
                break;
            case 2:
                food = dailyRecipe.getDinner().getByIndex(itemIndex);
                break;
        }
        if (food!=null)
        {
            if (systemModel.getUserData().getFavoriteFoodList().hasFood(food))
            {
                systemModel.removeFavoriteFood(food);
            }
            else
            {
                systemModel.addFavoriteFood(food);
            }
        }
    }

    public void deleteFood(int listIndex, int itemIndex)
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
        if (this.showDate!=showDate)
        {
            this.showDate = showDate;
            showDateText.setValue(this.showDate.getMonthValue()+"/"+this.showDate.getDayOfMonth());
            DailyRecipe dailyRecipe = systemModel.getUserData().getMyDailyRecipeList().getByDate(showDate);
            if (dailyRecipe==null)
            {
                dailyRecipe = new DailyRecipe(showDate);
            }
            showRecipe.setValue(dailyRecipe);
        }
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
            case "updateFavoriteFoodList":
                favouriteFoodList.setValue(systemModel.getUserData().getFavoriteFoodList());
                break;
            case "updateFood":
                showRecipe.setValue(systemModel.getUserData().getMyDailyRecipeList().getByDate(showDate));
                favouriteFoodList.setValue(systemModel.getUserData().getFavoriteFoodList());
                break;
        }
    }
}
