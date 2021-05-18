package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.utility.MyString;
import com.example.week_recipe.view.adapter.WeekRecipeListAdapter;

public class SearchWeekRecipeFragment extends Fragment {

    private View view;
    private EditText nameEditText;
    private CardView clearTextCardView;
    private CardView backCardView;
    private WeekRecipeListFragment fragment;
    private FavouriteWeekRecipeList basicWeekRecipeList;
    private FavouriteWeekRecipeList showList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_search_week_recipe, container, false);
    }

    public void bind(FavouriteWeekRecipeList basicWeekRecipeList, WeekRecipeListAdapter.OnItemClickListener listener)
    {
        this.basicWeekRecipeList = basicWeekRecipeList;
        showList = this.basicWeekRecipeList;
        nameEditText = view.findViewById(R.id.fragment_searchWeekRecipe_nameEditText);
        clearTextCardView = view.findViewById(R.id.fragment_searchWeekRecipe_clearTextCardView);
        backCardView = view.findViewById(R.id.fragment_searchWeekRecipe_backCardView);
        fragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_searchWeekRecipe_fragment));
        fragment.bind(showList,listener,false,false);
        setListener();
        showSearchResult(true);
        updateClearTextCardViewVisibility();
    }

    private void setListener()
    {
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showSearchResult(false);
                updateClearTextCardViewVisibility();
            }
        });
        clearTextCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
            }
        });
        backCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @SuppressLint("SwitchIntDef")
    private void updateClearTextCardViewVisibility()
    {
        String searchText = nameEditText.getText().toString();
        switch (clearTextCardView.getVisibility())
        {
            case View.VISIBLE:
                if (MyString.isNullOrEmpty(searchText))
                {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                    alphaAnimation.setDuration(500);
                    clearTextCardView.setAnimation(alphaAnimation);
                    clearTextCardView.setVisibility(View.GONE);
                }
                break;
            case View.GONE:
                if (!MyString.isNullOrEmpty(searchText))
                {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                    alphaAnimation.setDuration(500);
                    clearTextCardView.setAnimation(alphaAnimation);
                    clearTextCardView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void showSearchResult(boolean showAnimation)
    {
        String searchText = nameEditText.getText().toString();
        if (MyString.isNullOrEmpty(searchText))
        {
            showList = basicWeekRecipeList;
        }
        else
        {
            showList = basicWeekRecipeList.getListByName(searchText);
        }
        fragment.updateWeekRecipeList(showList,showAnimation);
    }

    private void clearText()
    {
        nameEditText.setText(null);
    }

    private void back()
    {
        getActivity().finish();
    }

    public void updateBasicFoodList(FavouriteWeekRecipeList basicWeekRecipeList)
    {
        this.basicWeekRecipeList = basicWeekRecipeList;
        showSearchResult(true);
    }

    public FavouriteWeekRecipeList getShowList() {
        return showList;
    }

    public EditText getNameEditText() {
        return nameEditText;
    }
}