package com.example.week_recipe.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.week_recipe.R;
import com.example.week_recipe.adapter.FoodListAdapter;
import com.example.week_recipe.adapter.HorizontalIngredientsListAdapter;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.view.layoutManager.MyLinearLayoutManager;

import java.util.ArrayList;

public class HorizontalIngredientsListFragment extends Fragment {

    private RecyclerView horizontalIngredientsListView;
    private View view;
    private IngredientsList ingredientsList;
    private HorizontalIngredientsListAdapter adapter;
    private IngredientsList overflowList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_horizontal_ingredients_list, container, false);
        return view;
    }

    public HorizontalIngredientsListFragment(View view)
    {
        this.view = view;
    }

    public void bind(IngredientsList ingredientsList, boolean needSet, HorizontalIngredientsListAdapter.OnItemClickListener listener)
    {
        overflowList = null;
        if (ingredientsList==null)
        {
            this.ingredientsList = new IngredientsList();
        }
        else
        {
            this.ingredientsList = ingredientsList;
        }
        adapter = new HorizontalIngredientsListAdapter(this.ingredientsList,needSet,listener);
        horizontalIngredientsListView = view.findViewById(R.id.fragment_horizontalIngredientsList_recyclerView);
        horizontalIngredientsListView.hasFixedSize();
        MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(view.getContext());
        layoutManager.setScrollEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalIngredientsListView.setLayoutManager(layoutManager);
        horizontalIngredientsListView.setAdapter(adapter);
        if (this.ingredientsList.getSize()==0) {
            horizontalIngredientsListView.setVisibility(View.GONE);
        }
        else
        {
            horizontalIngredientsListView.setVisibility(View.VISIBLE);
        }
    }

    public void reShow()
    {
        adapter.updateIngredientsList(ingredientsList,false);
        horizontalIngredientsListView.setAdapter(adapter);
    }

    public IngredientsList getOverflowList()
    {
        if (overflowList == null)
        {
            IngredientsList showList = new IngredientsList();
            overflowList = ingredientsList.copy();
            int sizeOfShow = adapter.getSizeOfViewHolderList()-1;
            if (adapter.getSizeOfViewHolderList()>ingredientsList.getSize())
            {
                sizeOfShow+=1;
            }
            for (int x=0;x<sizeOfShow;x++)
            {
                showList.add(ingredientsList.getByIndex(x));
            }
            adapter.updateIngredientsList(showList,false);
            horizontalIngredientsListView.setAdapter(adapter);

            for (int x=0;x<showList.getSize();x++)
            {
                overflowList.remove(showList.getByIndex(x));
            }
            ingredientsList = showList;
            System.out.println("over:"+overflowList.getSize());
        }
        return overflowList;
    }
}