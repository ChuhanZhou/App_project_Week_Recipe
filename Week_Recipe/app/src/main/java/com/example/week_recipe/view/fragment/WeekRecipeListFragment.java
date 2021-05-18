package com.example.week_recipe.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.view.adapter.RecipeListAdapter;
import com.example.week_recipe.view.adapter.WeekRecipeListAdapter;

import java.time.LocalDate;

public class WeekRecipeListFragment extends Fragment {

    private View view;
    private FavouriteWeekRecipeList weekRecipeList;
    private RecyclerView recipeListView;
    private TextView noDataTextView;
    private WeekRecipeListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_week_recipe_list, container, false);
    }

    public void bind(FavouriteWeekRecipeList weekRecipeList, WeekRecipeListAdapter.OnItemClickListener listener,boolean hasMore, boolean hasDelete)
    {
        this.weekRecipeList = weekRecipeList;
        adapter = new WeekRecipeListAdapter(this.weekRecipeList,listener,hasMore,hasDelete);
        recipeListView = view.findViewById(R.id.fragment_weekRecipeList_recyclerView);
        noDataTextView = view.findViewById(R.id.fragment_weekRecipeList_noDataTextView);

        setNoDataTextViewVisibility();

        recipeListView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recipeListView.setLayoutManager(layoutManager);
        recipeListView.setAdapter(adapter);
    }

    public void updateWeekRecipeList(FavouriteWeekRecipeList weekRecipeList,boolean showAnimation)
    {
        this.weekRecipeList = weekRecipeList;
        adapter.updateWeekRecipeList(this.weekRecipeList,showAnimation);
        recipeListView.setAdapter(adapter);
    }

    private void setNoDataTextViewVisibility()
    {
        if (weekRecipeList==null||weekRecipeList.getSize()==0)
        {
            noDataTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noDataTextView.setVisibility(View.GONE);
        }
    }
}