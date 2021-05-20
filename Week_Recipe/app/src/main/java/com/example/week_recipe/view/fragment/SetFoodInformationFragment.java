package com.example.week_recipe.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayoutStates;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.utility.MyPicture;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.net.URI;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SetFoodInformationFragment extends Fragment {

    private final static int IMAGE_FROM_PHOTO = 1001;
    private View view;
    private ImageView foodImage;
    private EditText foodNameEditText;
    private TextView foodNameTextView;
    private Spinner foodTypeSpinner;
    private IngredientsListFragment ingredientsListFragment;
    private IngredientsList ingredientsList;
    private FloatingActionButton chooseImageButton;
    private NestedScrollView nestedScrollView;
    private boolean lockName;
    private boolean updateImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_set_food_information, container, false);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case IMAGE_FROM_PHOTO:
                if (data!=null)
                {
                    Uri uri = data.getData();
                    foodImage.setImageURI(uri);
                    //make sure the image can be shown when the image is too big
                    foodImage.setImageBitmap(MyPicture.drawableToBitmap(foodImage.getDrawable()));
                    foodImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    updateImage = true;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void bind(Food food,boolean lockName)
    {
        updateImage = false;
        this.lockName = lockName;
        foodImage = view.findViewById(R.id.fragment_setFoodInformation_foodImage);
        foodNameEditText = view.findViewById(R.id.fragment_setFoodInformation_foodNameEditText);
        foodNameTextView = view.findViewById(R.id.fragment_setFoodInformation_foodNameTextView);
        foodTypeSpinner = view.findViewById(R.id.fragment_setFoodInformation_foodTypeSpinner);
        ingredientsListFragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_setFoodInformation_ingredientsListFragment));
        chooseImageButton = view.findViewById(R.id.fragment_setFoodInformation_chooseImageButton);
        nestedScrollView = view.findViewById(R.id.fragment_setFoodInformation_scrollView);
        setListener();
        updateNameView();

        if (food.hasImage())
        {
            foodImage.setImageBitmap(food.getImage());
            foodImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            foodImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        foodNameEditText.setText(food.getName());
        ingredientsList = food.getIngredientsList();

        for (int x=0;x<FoodType.values().length;x++)
        {
            if (FoodType.values()[x]==food.getType())
            {
                foodTypeSpinner.setSelection(x);
                break;
            }
        }

        ingredientsListFragment.bind(ingredientsList,true);
    }

    public void bind(String foodName,boolean lockName)
    {
        Food food = new Food(foodName,FoodType.Meat,null);
        bind(food,lockName);
    }

    private void setListener()
    {
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromLocal();
            }
        });
        foodNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                foodNameTextView.setText(foodNameEditText.getText());
            }
        });
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone((ConstraintLayout) ingredientsListFragment.getView());
                constraintSet.setMargin(R.id.fragment_ingredientsList_loadView,ConstraintSet.TOP,scrollY+30);
                constraintSet.applyTo((ConstraintLayout) ingredientsListFragment.getView());
            }
        });
    }

    private void updateNameView()
    {
        if (lockName)
        {
            foodNameEditText.setVisibility(View.GONE);
            foodNameTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            foodNameEditText.setVisibility(View.VISIBLE);
            foodNameTextView.setVisibility(View.GONE);
        }
    }

    private void selectImageFromLocal() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_FROM_PHOTO);
    }

    public Food getNewFood()
    {
        FoodType newFoodType = FoodType.values()[foodTypeSpinner.getSelectedItemPosition()];

        if (foodImage.getScaleType()==ImageView.ScaleType.FIT_CENTER)
        {
            return new Food(foodNameTextView.getText().toString(),newFoodType,ingredientsList);
        }
        Food newFood = new Food(foodNameTextView.getText().toString(),newFoodType,ingredientsList,MyPicture.drawableToBitmap(foodImage.getDrawable()));
        if (updateImage)
        {
            MyPicture.putBitmapToOnlineDatabase(newFood.getImageId());
        }
        return newFood;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}