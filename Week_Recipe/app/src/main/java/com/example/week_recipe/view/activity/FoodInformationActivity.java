package com.example.week_recipe.view.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.view.fragment.FoodInformationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FoodInformationActivity extends AppCompatActivity {

    public static final String foodMenuKey = "foodMenu";
    private Button backButton;
    private FloatingActionButton editButton;
    private FoodInformationFragment fragment;
    private Food showFood;
    private boolean clickEditButton;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        bind();
        setListener();
    }

    @Override
    public void onBackPressed() {
        setResult(1,getIntent());
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clickEditButton = false;
        }).start();
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == 1)
        {
            showFood = (Food) UiDataCache.getData(data.getExtras().getString("showFood"));
            fragment.bind(showFood);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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