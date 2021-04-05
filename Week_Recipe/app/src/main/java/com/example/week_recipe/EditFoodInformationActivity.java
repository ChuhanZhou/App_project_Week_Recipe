package com.example.week_recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.week_recipe.ViewModel.EditFoodInformationViewModel;
import com.example.week_recipe.ViewModel.RecipeWithDateViewModel;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.utility.UiDataCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class EditFoodInformationActivity extends AppCompatActivity {

    private EditFoodInformationViewModel viewModel;
    private Button backButton;
    private FloatingActionButton saveButton;
    private Food oldFood;
    private SetFoodInformationFragment fragment;
    private boolean clickSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_information);
        //init
        clickSaveButton = false;
        viewModel = new ViewModelProvider(this).get(EditFoodInformationViewModel.class);

        bind();
        setListener();
    }

    private void toastPrint(String information)
    {
        Context context = getApplicationContext();
        Toast.makeText(context,information,Toast.LENGTH_SHORT).show();
    }

    private void bind()
    {
        backButton = findViewById(R.id.editFoodInformation_backButton);
        saveButton = findViewById(R.id.editFoodInformation_saveButton);
        fragment = FragmentManager.findFragment(findViewById(R.id.editFoodInformation_fragment));
        oldFood = (Food) UiDataCache.getData(getIntent().getExtras().getString("editFood"));
        fragment.bind(oldFood.copy());
    }

    private void setListener()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackButton();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSaveButton();
            }
        });
    }

    private void clickBackButton()
    {
        finish();
    }

    private void clickSaveButton()
    {
        if (!clickSaveButton)
        {
            clickSaveButton = true;
            Food newFood = fragment.getNewFood();
            //Food newFood = oldFood;

            String result = viewModel.updateFoodInformation(oldFood,newFood);
            if (result!=null)
            {
                toastPrint(result);
                clickSaveButton = false;
            }
            else
            {
                Intent intent = new Intent();
                intent.putExtra("updateShowFood",UiDataCache.putData("updateShowFood",newFood));
                setResult(1,intent);
                finish();
            }
        }
    }
}