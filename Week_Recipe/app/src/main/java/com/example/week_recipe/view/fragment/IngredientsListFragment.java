package com.example.week_recipe.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.adapter.HorizontalIngredientsListAdapter;
import com.example.week_recipe.adapter.IngredientsListAdapter;
import com.example.week_recipe.model.domain.food.Ingredients;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.utility.MyString;
import com.example.week_recipe.view.layoutManager.MyLinearLayoutManager;

public class IngredientsListFragment extends Fragment implements HorizontalIngredientsListAdapter.OnItemClickListener {
    private View view;
    private static IngredientsList ingredientsList;
    private IngredientsListAdapter adapter;
    private RecyclerView ingredientsListView;
    private TextView noDataTextView;
    private EditText nameEditText;
    private Button addButton;
    private boolean needSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
        return view;
    }

    private void toastPrint(String info)
    {
        if (!MyString.isNullOrEmptyOrFullOfSpace(info))
        {
            Toast.makeText(getContext(),info,Toast.LENGTH_SHORT).show();
        }
    }

    public void bind(IngredientsList ingredientsList, boolean needSet)
    {
        this.ingredientsList = ingredientsList;
        this.needSet = needSet;
        adapter = new IngredientsListAdapter(this.ingredientsList,needSet,this);
        ingredientsListView = view.findViewById(R.id.fragment_ingredientsList_recyclerView);
        noDataTextView = view.findViewById(R.id.fragment_ingredientsList_noDataTextView);
        nameEditText = view.findViewById(R.id.fragment_ingredientsList_nameEditText);
        addButton = view.findViewById(R.id.fragment_ingredientsList_addIngredientButton);
        setNoDataTextViewVisibility();
        updateStatus();
        setListener();

        ingredientsListView.hasFixedSize();
        MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(view.getContext());
        layoutManager.setScrollEnabled(false);
        ingredientsListView.setLayoutManager(layoutManager);
        ingredientsListView.setAdapter(adapter);
    }

    private void setListener()
    {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddButton();
            }
        });
    }

    private void updateStatus()
    {
        if (needSet)
        {
            nameEditText.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
        }
        else
        {
            nameEditText.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
        }
    }

    private void setNoDataTextViewVisibility()
    {
        if (ingredientsList==null||ingredientsList.getSize()==0)
        {
            noDataTextView.setVisibility(View.VISIBLE);
            ingredientsListView.setVisibility(View.GONE);
        }
        else
        {
            noDataTextView.setVisibility(View.GONE);
            ingredientsListView.setVisibility(View.VISIBLE);
        }
    }

    public void updateIngredientsList(IngredientsList ingredientsList)
    {
        if (ingredientsList==null)
        {
            this.ingredientsList = new IngredientsList();
        }
        else
        {
            this.ingredientsList = ingredientsList;
        }
        updateIngredientsList();
    }

    private void updateIngredientsList()
    {
        setNoDataTextViewVisibility();
        adapter.updateIngredientsList(ingredientsList);
        ingredientsListView.setAdapter(adapter);
    }

    public IngredientsList getIngredientsList()
    {
        return ingredientsList;
    }

    public void clickAddButton()
    {
        String name = nameEditText.getText().toString();
        String result;
        if (!MyString.isNullOrEmptyOrFullOfSpace(name))
        {
            result = ingredientsList.add(new Ingredients(name));
            if (result==null)
            {
                nameEditText.setText("");
                updateIngredientsList();
            }
        }
        else
        {
            result = "The name of ingredient can't be empty";
        }
        toastPrint(result);
    }

    @Override
    public void onItemClick(Ingredients ingredients) {
        if (needSet)
        {
            ingredientsList.remove(ingredients);
            updateIngredientsList();
        }
    }
}