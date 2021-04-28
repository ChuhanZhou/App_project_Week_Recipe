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
        ingredientsListFragment.bind(food.getIngredientsList(),false);
    }
}