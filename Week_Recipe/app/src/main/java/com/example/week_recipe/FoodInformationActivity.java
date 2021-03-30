package com.example.week_recipe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.week_recipe.model.domain.food.Food;
import com.google.gson.Gson;

public class FoodInformationActivity extends AppCompatActivity {

    private Button backButton;
    private View fragmentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        bind();
        setListener();
    }

    private void bind()
    {
        fragmentView = findViewById(R.id.foodInformation_fragment);
        backButton = findViewById(R.id.foodInformation_backButton);
        FoodInformationFragment fragment = FragmentManager.findFragment(fragmentView);
        Food showFood = new Gson().fromJson(getIntent().getExtras().getString("showFood"),Food.class);
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
    }

    private void clickBackButton()
    {
        finish();
    }
}