package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.Ingredients;
import com.example.week_recipe.model.domain.food.IngredientsList;


public class FoodInformationFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food_information, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(Food food)
    {
        ImageView foodImage = view.findViewById(R.id.fragment_foodInformation_foodImage);
        TextView foodNameTextView = view.findViewById(R.id.fragment_foodInformation_foodNameTextView);
        TextView foodTypeTextView = view.findViewById(R.id.fragment_foodInformation_foodTypeTextView);
        IngredientsListFragment ingredientsListFragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_foodInformation_ingredientsListFragment));

        if (food.hasImage())
        {
            foodImage.setImageBitmap(food.getImage());
            foodImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            foodImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        foodNameTextView.setText(food.getName());
        switch (food.getType())
        {
            case Meat:
                foodTypeTextView.setText(R.string.foodType_meat);
                break;
            case Vegetarian:
                foodTypeTextView.setText(R.string.foodType_vegetarian);
                break;
            case Other:
                foodTypeTextView.setText(R.string.foodType_other);
                break;
            default:
                foodTypeTextView.setText(food.getType().toString());
                break;
        }
        IngredientsList ingredientsList = new IngredientsList();
        ingredientsList.add(new Ingredients("AAAAAAAAAAAAAAAA"));
        ingredientsList.add(new Ingredients("BB"));
        ingredientsList.add(new Ingredients("CCC"));
        ingredientsList.add(new Ingredients("DDDD"));
        ingredientsList.add(new Ingredients("EEEEE"));
        ingredientsList.add(new Ingredients("FFFFFF"));
        ingredientsList.add(new Ingredients("G"));
        ingredientsList.add(new Ingredients("HHHHHHHHHHH"));
        ingredientsList.add(new Ingredients("IIIIIII"));
        ingredientsList.add(new Ingredients("1"));
        ingredientsList.add(new Ingredients("22"));
        ingredientsList.add(new Ingredients("333333333333"));
        ingredientsList.add(new Ingredients("3333879333"));
        ingredientsList.add(new Ingredients("138764233"));
        ingredientsList.add(new Ingredients("1454354233"));
        ingredientsList.add(new Ingredients("54354235"));
        ingredientsList.add(new Ingredients("re4545ev4235"));
        ingredientsList.add(new Ingredients("4235"));
        ingredientsList.add(new Ingredients("933ert"));
        ingredientsList.add(new Ingredients("wr933eert"));
        ingredientsList.add(new Ingredients("test1"));
        ingredientsList.add(new Ingredients("test2"));
        ingredientsList.add(new Ingredients("test3"));
        ingredientsList.add(new Ingredients("test4"));
        ingredientsList.add(new Ingredients("test5"));
        ingredientsList.add(new Ingredients("test6"));
        ingredientsList.add(new Ingredients("test7"));
        ingredientsList.add(new Ingredients("test8"));
        ingredientsList.add(new Ingredients("test9"));
        ingredientsList.add(new Ingredients("test10"));
        ingredientsList.add(new Ingredients("test11"));
        ingredientsList.add(new Ingredients("test12"));
        ingredientsList.add(new Ingredients("test13"));
        ingredientsList.add(new Ingredients("test14"));
        ingredientsList.add(new Ingredients("test15"));
        ingredientsList.add(new Ingredients("test16"));
        ingredientsList.add(new Ingredients("test17"));

        ingredientsListFragment.bind(ingredientsList,false);
        //ingredientsListFragment.bind(food.getIngredientsList(),false);
    }
}