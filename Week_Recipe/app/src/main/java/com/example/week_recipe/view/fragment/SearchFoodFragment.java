package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;

import com.example.week_recipe.R;
import com.example.week_recipe.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.utility.MyString;

public class SearchFoodFragment extends Fragment {

    private View view;
    private EditText foodNameEditText;
    private CardView clearTextCardView;
    private CardView backCardView;
    private FoodList basicFoodList;
    private FoodListFragment fragment;
    private FoodList showList;

    private FoodListAdapter.OnFoodListItemClickListener listener;
    private boolean hasMore;
    private boolean hasDelete;
    private boolean hasLike;
    private LiveData<FoodList> favouriteFoodList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_food, container, false);
        return view;
    }

    public void bind(FoodList basicFoodList, FoodListAdapter.OnFoodListItemClickListener listener,boolean hasMore, boolean hasDelete, boolean hasLike, LiveData<FoodList> favouriteFoodList)
    {
        this.basicFoodList = basicFoodList;
        showList = basicFoodList;
        this.listener = listener;
        this.hasMore = hasMore;
        this.hasDelete = hasDelete;
        this.hasLike = hasLike;
        this.favouriteFoodList = favouriteFoodList;

        foodNameEditText = view.findViewById(R.id.fragment_searchFood_foodNameEditText);
        clearTextCardView = view.findViewById(R.id.fragment_searchFood_clearTextCardView);
        backCardView = view.findViewById(R.id.fragment_searchFood_backCardView);
        fragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_searchFood_fragment));
        fragment.bind(showList,listener,hasMore,hasDelete,hasLike,favouriteFoodList);
        setListener();
        showSearchResult(true);
        updateClearTextCardViewVisibility();
    }

    private void setListener()
    {
        foodNameEditText.addTextChangedListener(new TextWatcher() {
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
        String searchText = foodNameEditText.getText().toString();
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
        String searchText = foodNameEditText.getText().toString();
        if (MyString.isNullOrEmpty(searchText))
        {
            showList = basicFoodList;
        }
        else
        {
            showList = basicFoodList.getListByName(searchText);
        }
        fragment.updateFoodList(showList,showAnimation);
    }

    private void clearText()
    {
        foodNameEditText.setText(null);
    }

    private void back()
    {
        getActivity().finish();
    }

    public void updateBasicFoodList(FoodList basicFoodList)
    {
        this.basicFoodList = basicFoodList;
        showSearchResult(true);
    }

    public boolean foodNameCanBeUsed()
    {
        String searchText = foodNameEditText.getText().toString();
        if (MyString.isNullOrEmpty(searchText))
        {
            return false;
        }
        return basicFoodList.getByName(searchText)==null;
    }

    public FoodList getShowList() {
        return showList;
    }

    public EditText getFoodNameEditText() {
        return foodNameEditText;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}