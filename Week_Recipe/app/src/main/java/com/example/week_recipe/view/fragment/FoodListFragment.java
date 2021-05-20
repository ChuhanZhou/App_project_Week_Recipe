package com.example.week_recipe.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.view.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.FoodList;

public class FoodListFragment extends Fragment{
    public static String noDataTitleIdKey = "foodListFragment_noDataTitleId";
    private View view;
    private TextView noDataTextView;
    private FoodList foodList;
    private FoodListAdapter foodListAdapter;
    private RecyclerView foodListView;
    private boolean needReverse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food_list, container, false);
        return view;
    }

    public void bind(FoodList foodList, FoodListAdapter.OnFoodListItemClickListener listener, boolean hasMore, boolean hasDelete, boolean hasLike, LiveData<FoodList> favouriteFoodList)
    {
        bind(false,foodList, listener, hasMore, hasDelete, hasLike, favouriteFoodList);
    }

    public void bind(boolean needReverse, FoodList foodList,FoodListAdapter.OnFoodListItemClickListener listener, boolean hasMore, boolean hasDelete, boolean hasLike, LiveData<FoodList> favouriteFoodList)
    {
        this.needReverse = needReverse;
        if (this.needReverse)
        {
            foodList = foodList.getReverse();
        }
        this.foodList = foodList;
        foodListView = view.findViewById(R.id.fragment_foodList_recyclerView);
        foodListAdapter = new FoodListAdapter(needReverse,this.foodList,listener,hasMore,hasDelete,hasLike,favouriteFoodList.getValue());
        noDataTextView = view.findViewById(R.id.fragment_foodList_noDataTextView);

        setNoDataTextViewVisibility();
        if (UiDataCache.getData(noDataTitleIdKey)!=null)
        {
            noDataTextView.setText((Integer) UiDataCache.getData(noDataTitleIdKey));
            UiDataCache.putData(noDataTitleIdKey,null);
        }

        foodListView.hasFixedSize();
        foodListView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        foodListView.setAdapter(foodListAdapter);
        addListener(favouriteFoodList);
    }

    public void updateFoodList(FoodList foodList,boolean showAnimation)
    {
        if (this.needReverse)
        {
            foodList = foodList.getReverse();
        }
        this.foodList = foodList;
        foodListAdapter.updateFoodList(this.foodList,showAnimation);
        foodListView.setAdapter(foodListAdapter);
        setNoDataTextViewVisibility();
    }

    private void setNoDataTextViewVisibility()
    {
        if (foodList==null||foodList.getSize()==0)
        {
            noDataTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noDataTextView.setVisibility(View.GONE);
        }
    }

    private void addListener(LiveData<FoodList> favouriteFoodList)
    {
        if (favouriteFoodList!=null)
        {
            favouriteFoodList.observe(this, new Observer<FoodList>() {
                @Override
                public void onChanged(FoodList foodList) {
                    foodListAdapter.updateLike(foodList);
                }
            });
        }
    }
}