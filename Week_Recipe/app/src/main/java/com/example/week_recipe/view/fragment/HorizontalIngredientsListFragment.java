package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.week_recipe.R;
import com.example.week_recipe.adapter.HorizontalIngredientsListAdapter;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.view.layoutManager.MyLinearLayoutManager;

public class HorizontalIngredientsListFragment extends Fragment {

    private RecyclerView horizontalIngredientsListView;
    private View view;
    private IngredientsList ingredientsList;
    private HorizontalIngredientsListAdapter adapter;
    private IngredientsList overflowList;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            adapter.updateIngredientsList(ingredientsList,false);
            horizontalIngredientsListView.setAdapter(adapter);
        }
    };

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

    public void bind(IngredientsList ingredientsList,IngredientsList overflowList, boolean needSet, HorizontalIngredientsListAdapter.OnItemClickListener listener)
    {
        horizontalIngredientsListView = view.findViewById(R.id.fragment_horizontalIngredientsList_recyclerView);
        this.overflowList = overflowList;
        if (ingredientsList==null)
        {
            this.ingredientsList = new IngredientsList();
        }
        else
        {
            this.ingredientsList = ingredientsList;
        }
        if (overflowList!=null)
        {
            adapter = new HorizontalIngredientsListAdapter(this.ingredientsList,horizontalIngredientsListView,false,needSet,listener);
        }
        else
        {
            adapter = new HorizontalIngredientsListAdapter(this.ingredientsList,horizontalIngredientsListView,true,needSet,listener);
        }

        horizontalIngredientsListView.hasFixedSize();
        horizontalIngredientsListView.setNestedScrollingEnabled(false);
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
        handler.sendMessage(new Message());
    }

    public void setHeight(boolean isShow)
    {
        if (isShow)
        {
            horizontalIngredientsListView.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            horizontalIngredientsListView.getLayoutParams().height=1;
        }
    }

    public IngredientsList getShowList()
    {
        getOverflowList();
        return ingredientsList;
    }

    public IngredientsList getOverflowList()
    {
        if (overflowList == null)
        {
            IngredientsList showList = new IngredientsList();
            overflowList = ingredientsList.copy();
            int sizeOfShow = adapter.getSizeOfViewHolderList()-1;
            if (adapter.getSizeOfViewHolderList()==1||adapter.getSizeOfViewHolderList()>ingredientsList.getSize())
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
        }
        return overflowList;
    }
}