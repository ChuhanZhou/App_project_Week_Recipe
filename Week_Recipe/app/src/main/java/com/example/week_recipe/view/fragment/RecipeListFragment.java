package com.example.week_recipe.view.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.view.adapter.FoodListAdapter;
import com.example.week_recipe.view.adapter.RecipeListAdapter;
import com.example.week_recipe.view.layoutManager.MyLinearLayoutManager;

import java.time.LocalDate;

public class RecipeListFragment extends Fragment {

    private View view;
    private TextView noDataTextView;
    private RecipeList recipeList;
    private RecipeListAdapter adapter;
    private RecyclerView recipeListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(RecipeList recipeList, RecipeListAdapter.OnItemClickListener onItemClickListener)
    {
        this.recipeList = recipeList;
        recipeListView = view.findViewById(R.id.fragment_recipeList_recyclerView);
        adapter = new RecipeListAdapter(this.recipeList,onItemClickListener);
        noDataTextView = view.findViewById(R.id.fragment_recipeList_noDataTextView);

        setNoDataTextViewVisibility();

        recipeListView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recipeListView.setLayoutManager(layoutManager);
        recipeListView.setAdapter(adapter);
        if (recipeList.hasDate(LocalDate.now()))
        {
            recipeListView.scrollToPosition(LocalDate.now().getDayOfWeek().getValue()-1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateRecipeList(RecipeList recipeList)
    {
        this.recipeList = recipeList;
        adapter.updateRecipeList(recipeList);
        //recipeListView.setAdapter(foodListAdapter);
        setNoDataTextViewVisibility();
    }

    private void setNoDataTextViewVisibility()
    {
        if (recipeList==null||recipeList.getSize()==0)
        {
            noDataTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noDataTextView.setVisibility(View.GONE);
        }
    }
}