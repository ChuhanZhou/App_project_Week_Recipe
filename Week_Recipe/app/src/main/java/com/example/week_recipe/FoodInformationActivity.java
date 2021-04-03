package com.example.week_recipe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.week_recipe.model.domain.food.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class FoodInformationActivity extends AppCompatActivity {

    private Button backButton;
    private FloatingActionButton editButton;
    private FoodInformationFragment fragment;
    private Food showFood;
    private Gson gson;
    private boolean clickEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        clickEditButton = false;
        gson = new Gson();
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
            showFood = gson.fromJson(data.getExtras().getString("updateShowFood"),Food.class);
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
        this.showFood = gson.fromJson(getIntent().getExtras().getString("showFood"),Food.class);
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
            intent.putExtra("editFood", new Gson().toJson(showFood));
            startActivityForResult(intent,2);
        }
    }
}