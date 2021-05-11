package com.example.week_recipe.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.view.adapter.FoodListAdapter;

public class RecipeListFragment extends Fragment {

    private View view;
    private TextView noDataTextView;
    private RecipeList recipeList;
    //private FoodListAdapter foodListAdapter;
    private RecyclerView recipeListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

    public void bind(RecipeList recipeList)
    {
        this.recipeList = recipeList;
    }
}