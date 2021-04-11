package com.example.week_recipe.viewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.Food;
@RequiresApi(api = Build.VERSION_CODES.O)
public class EditFoodInformationViewModel extends ViewModel {
    private final SystemModel systemModel;

    public EditFoodInformationViewModel()
    {
        systemModel = SystemModelManager.getSystemModelManager();
    }

    public String updateFoodInformation(Food oldFood,Food newFood)
    {
        return systemModel.updateFoodOfAll(oldFood, newFood);
    }
}
