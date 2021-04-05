package com.example.week_recipe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.utility.UiDataCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class FoodInformationActivity extends AppCompatActivity {

    private Button backButton;
    private FloatingActionButton editButton;
    private FoodInformationFragment fragment;
    private Food showFood;
    private boolean clickEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        bind();
        setListener();
    }

    @Override
    protected void onRestart() {
        clickEditButton = false;
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == 1)
        {
            showFood = (Food) UiDataCache.getData(data.getExtras().getString("updateShowFood"));
            fragment.bind(showFood);
            clickEditButton = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bind()
    {
        View fragmentView = findViewById(R.id.foodInformation_fragment);
        backButton = findViewById(R.id.foodInformation_backButton);
        editButton = findViewById(R.id.foodInformation_editFoodButton);
        fragment = FragmentManager.findFragment(fragmentView);
        this.showFood = (Food) UiDataCache.getData(getIntent().getExtras().getString("showFood"));
        fragment.bind(showFood);
    }

    private void setListener()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackButton();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEditButton();
            }
        });
    }

    private void clickBackButton()
    {
        finish();
    }

    private void clickEditButton()
    {
        if (!clickEditButton)
        {
            clickEditButton = true;
            Intent intent = new Intent(getApplicationContext(),EditFoodInformationActivity.class);
            intent.putExtra("editFood", UiDataCache.putData("editFood",showFood));
            startActivityForResult(intent,2);
        }
    }
}