package com.example.week_recipe.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.view.fragment.SetFoodInformationFragment;
import com.example.week_recipe.viewModel.EditFoodInformationViewModel;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.utility.UiDataCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
@RequiresApi(api = Build.VERSION_CODES.O)
public class EditFoodInformationActivity extends AppCompatActivity {

    public static final String foodMenuKey = "foodMenu";
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
        if (information!=null)
        {
            Context context = getApplicationContext();
            Toast.makeText(context,information,Toast.LENGTH_SHORT).show();
        }
    }

    private void bind()
    {
        backButton = findViewById(R.id.editFoodInformation_backButton);
        saveButton = findViewById(R.id.editFoodInformation_saveButton);
        fragment = FragmentManager.findFragment(findViewById(R.id.editFoodInformation_fragment));
        oldFood = (Food) UiDataCache.getData(getIntent().getExtras().getString("editFood"));
        fragment.bind(oldFood.copy(),false);
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

            String result;
            FoodList foodMenu = (FoodList) UiDataCache.getData(foodMenuKey);
            if (foodMenu==null)
            {
                //for update food home page
                result = viewModel.updateFoodInformation(oldFood,newFood);
            }
            else
            {
                //for update food in favourite week recipe
                result = foodMenu.update(oldFood,newFood);
                if (result==null)
                {
                    viewModel.updateFoodInformation(oldFood,newFood);
                }
            }

            toastPrint(result);
            if (result==null)
            {
                Intent intent = new Intent();
                intent.putExtra("showFood",UiDataCache.putData("showFood",newFood));
                setResult(1,intent);
                finish();
            }
            else
            {
                clickSaveButton = false;
            }
        }
    }
}