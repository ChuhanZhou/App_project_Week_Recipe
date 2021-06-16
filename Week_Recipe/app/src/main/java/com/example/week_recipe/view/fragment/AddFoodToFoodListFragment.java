package com.example.week_recipe.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week_recipe.R;

import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.view.adapter.FoodListAdapter;
import com.example.week_recipe.model.domain.food.FoodList;
@RequiresApi(api = Build.VERSION_CODES.O)
public class AddFoodToFoodListFragment extends Fragment{

    private View view;
    private SearchFoodFragment searchFragment;
    private SetFoodInformationFragment setFoodInformationFragment;
    private TextView createFoodTextView;
    private LiveData<FoodList> basicFoodList;
    private boolean isSearching;
    private boolean clickFoodImage;
    private StateListener listener;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_food_to_food_list, container, false);
        return view;
    }

    public void bind(LiveData<FoodList> basicFoodList,LiveData<FoodList> favouriteFoodList,StateListener listener)
    {
        bind(false,basicFoodList, favouriteFoodList, listener);
    }

    public void bind(boolean needReverse,LiveData<FoodList> basicFoodList,LiveData<FoodList> favouriteFoodList,StateListener listener)
    {
        isSearching = true;
        clickFoodImage = false;
        searchFragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_addFoodToFoodList_searchFragment));
        setFoodInformationFragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_addFoodToFoodList_setFoodInformationFragment));
        createFoodTextView = view.findViewById(R.id.fragment_addFoodToFoodList_createFoodTextView);
        this.basicFoodList = basicFoodList;
        this.listener = listener;

        searchFragment.bind(needReverse,basicFoodList.getValue(),listener,false,false,true,favouriteFoodList);
        updateFragment();
        updateCreateFoodTextViewVisibility();
        setListener();
    }

    public boolean onBackPressed() {
        if (isSearching)
        {
            return true;
        }
        else
        {
            isSearching = true;
            updateFragment();
            return false;
        }
    }

    private void setListener()
    {
        basicFoodList.observe(this, new Observer<FoodList>() {
            @Override
            public void onChanged(FoodList foodList) {
                searchFragment.updateBasicFoodList(foodList,true);
            }
        });
        createFoodTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFood();
            }
        });
        searchFragment.getFoodNameEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCreateFoodTextViewVisibility();
            }
        });
    }

    private void updateCreateFoodTextViewVisibility()
    {
        if (isSearching)
        {
            if (searchFragment.foodNameCanBeUsed())
            {
                showCreateFoodTextView();
            }
            else
            {
                hidCreateFoodTextView();
            }
        }
        else
        {
            showCreateFoodTextView();
        }
    }

    private void showCreateFoodTextView()
    {
        if (createFoodTextView.getVisibility()==View.GONE)
        {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            translateAnimation.setDuration(200);
            createFoodTextView.setAnimation(translateAnimation);
            createFoodTextView.setVisibility(View.VISIBLE);
        }
    }

    private void hidCreateFoodTextView()
    {
        if (createFoodTextView.getVisibility()==View.VISIBLE)
        {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);
            translateAnimation.setDuration(200);
            createFoodTextView.setAnimation(translateAnimation);
            createFoodTextView.setVisibility(View.GONE);
        }
    }

    public void updateFragment(boolean isSearching)
    {
        this.isSearching = isSearching;
        updateFragment();
    }

    private void updateFragment()
    {
        if (isSearching)
        {
            searchFragment.getView().setVisibility(View.VISIBLE);
            setFoodInformationFragment.getView().setVisibility(View.GONE);
        }
        else
        {
            searchFragment.getView().setVisibility(View.GONE);
            setFoodInformationFragment.getView().setVisibility(View.VISIBLE);
        }
    }

    private void createFood()
    {
        if (isSearching)
        {
            isSearching = false;
            setFoodInformationFragment.bind(searchFragment.getFoodNameEditText().getText().toString(),true);
            updateFragment();
        }
        else
        {
            listener.createFood(setFoodInformationFragment.getNewFood());
        }
    }

    public void clearSearchText()
    {
        searchFragment.clearText();
    }

    public FoodList getShowList()
    {
        return searchFragment.getShowList();
    }

    public EditText getFoodNameEditText() {
        if (searchFragment!=null)
        {
            return searchFragment.getFoodNameEditText();
        }
        return null;
    }

    public interface StateListener extends FoodListAdapter.OnFoodListItemClickListener{
        void createFood(Food newFood);
    }
}