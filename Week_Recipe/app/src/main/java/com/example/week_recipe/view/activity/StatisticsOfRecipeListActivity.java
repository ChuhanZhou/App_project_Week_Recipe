package com.example.week_recipe.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.view.fragment.IngredientsListFragment;
import com.example.week_recipe.view.myView.SectorGraphItemInfo;
import com.example.week_recipe.view.myView.SectorGraphView;

import java.util.HashMap;
import java.util.Map;

public class StatisticsOfRecipeListActivity extends AppCompatActivity {
    public static String showValueKey = "statisticsOfRecipeListActivity_recipeList";
    private SectorGraphView sectorGraphView;
    private NestedScrollView nestedScrollView;
    private Button backButton;
    private IngredientsListFragment ingredientsListFragment;
    private Map<FoodType,Integer> numOfFoodTypeMap;

    private RecipeList recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_of_recipe_list);

        init();
        bind();
        setListener();
    }

    private void init()
    {
        recipeList = (RecipeList) UiDataCache.getData(showValueKey);
        numOfFoodTypeMap = new HashMap<>();
        for (int x=0;x<FoodType.values().length;x++)
        {
            numOfFoodTypeMap.put(FoodType.values()[x],0);
        }
    }

    private void bind()
    {
        nestedScrollView = findViewById(R.id.statisticsOfRecipeList_nestedScrollView);
        sectorGraphView = findViewById(R.id.statisticsOfRecipeList_sectorGraphView);
        backButton = findViewById(R.id.statisticsOfRecipeList_backButton);
        ingredientsListFragment = FragmentManager.findFragment(findViewById(R.id.statisticsOfRecipeList_ingredientsListFragment));

        setInfoToIngredientsListFragment();
        setInfoToSectorGraphView();
    }

    private void setListener()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setInfoToIngredientsListFragment()
    {
        IngredientsList showList = new IngredientsList();
        FoodList all = recipeList.getFoodMenu();
        for (int x=0;x<all.getSize();x++)
        {
            for (int i=0;i<all.getByIndex(x).getIngredientsList().getSize();i++)
            {
                showList.add(all.getByIndex(x).getIngredientsList().getByIndex(i));
            }
        }
        ingredientsListFragment.bind(showList,false);
    }

    private void setInfoToSectorGraphView()
    {
        for (int x=0;x<recipeList.getSize();x++)
        {
            for (int i=0;i<recipeList.getByIndex(x).getBreakfast().getSize();i++)
            {
                FoodType foodType = recipeList.getByIndex(x).getBreakfast().getByIndex(i).getType();
                int numOfType = numOfFoodTypeMap.get(foodType)+1;
                numOfFoodTypeMap.put(foodType,numOfType);
            }
            for (int i=0;i<recipeList.getByIndex(x).getLunch().getSize();i++)
            {
                FoodType foodType = recipeList.getByIndex(x).getLunch().getByIndex(i).getType();
                int numOfType = numOfFoodTypeMap.get(foodType)+1;
                numOfFoodTypeMap.put(foodType,numOfType);
            }
            for (int i=0;i<recipeList.getByIndex(x).getDinner().getSize();i++)
            {
                FoodType foodType = recipeList.getByIndex(x).getDinner().getByIndex(i).getType();
                int numOfType = numOfFoodTypeMap.get(foodType)+1;
                numOfFoodTypeMap.put(foodType,numOfType);
            }
        }

        int total = 0;
        for (int x=0;x<FoodType.values().length;x++)
        {
            total += numOfFoodTypeMap.get(FoodType.values()[x]);
        }

        for (int x=0;x<FoodType.values().length;x++)
        {
            if (numOfFoodTypeMap.get(FoodType.values()[x])>0)
            {
                sectorGraphView.addItemInfo(new SectorGraphItemInfo(getText(Food.getStringIdByFoodType(FoodType.values()[x])).toString(),(float)numOfFoodTypeMap.get(FoodType.values()[x])/(float)total));
            }
        }
        sectorGraphView.draw(40,true,75);
    }
}