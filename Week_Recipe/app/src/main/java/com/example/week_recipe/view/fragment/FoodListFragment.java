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
import com.example.week_recipe.view.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.FoodList;

public class FoodListFragment extends Fragment{

    private View view;
    private TextView noDataTextView;
    private FoodList foodList;
    private FoodListAdapter foodListAdapter;
    private RecyclerView foodListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food_list, container, false);
        return view;
    }

    @Override
    public void onPause() {
        foodListAdapter.stopAnimation();
        super.onPause();
    }

    public void bind(FoodList foodList, FoodListAdapter.OnFoodListItemClickListener listener, boolean hasMore, boolean hasDelete, boolean hasLike, LiveData<FoodList> favouriteFoodList)
    {
        this.foodList = foodList;
        foodListView = view.findViewById(R.id.fragment_foodList_recyclerView);
        foodListAdapter = new FoodListAdapter(this.foodList,listener,hasMore,hasDelete,hasLike,favouriteFoodList.getValue());
        noDataTextView = view.findViewById(R.id.fragment_foodList_noDataTextView);

        setNoDataTextViewVisibility();

        foodListView.hasFixedSize();
        foodListView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        foodListView.setAdapter(foodListAdapter);
        addListener(favouriteFoodList);
    }

    public void updateFoodList(FoodList foodList,boolean showAnimation)
    {
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