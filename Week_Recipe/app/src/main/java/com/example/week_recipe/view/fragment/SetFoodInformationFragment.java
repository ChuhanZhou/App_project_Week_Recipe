package com.example.week_recipe.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodType;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.utility.MyPicture;
import com.example.week_recipe.utility.UiDataCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
@RequiresApi(api = Build.VERSION_CODES.O)
public class SetFoodInformationFragment extends Fragment {

    private final static int IMAGE_FROM_PHOTO = 1001;
    private View view;
    private ImageView foodImage;
    private EditText foodNameEditText;
    private TextView foodNameTextView;
    private Spinner foodTypeSpinner;
    private TextView foodIngredientsTextView;
    private IngredientsList ingredientsList;
    private FloatingActionButton chooseImageButton;
    private boolean lockName;

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
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void bind(Food food,boolean lockName)
    {
        this.lockName = lockName;
        foodImage = view.findViewById(R.id.fragment_setFoodInformation_foodImage);
        foodNameEditText = view.findViewById(R.id.fragment_setFoodInformation_foodNameEditText);
        foodNameTextView = view.findViewById(R.id.fragment_setFoodInformation_foodNameTextView);
        foodTypeSpinner = view.findViewById(R.id.fragment_setFoodInformation_foodTypeSpinner);
        foodIngredientsTextView = view.findViewById(R.id.fragment_setFoodInformation_foodIngredientsTextView);
        chooseImageButton = view.findViewById(R.id.fragment_setFoodInformation_chooseImageButton);
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

        switch (food.getType())
        {
            case Meat:
                foodTypeSpinner.setSelection(0);
                break;
            case Vegetarian:
                foodTypeSpinner.setSelection(1);
                break;
            case Other:
                foodTypeSpinner.setSelection(2);
                break;
            default:
                foodTypeSpinner.setSelection(2);
                break;
        }

        if (!ingredientsList.isEmpty())
        {
            System.out.println(2);
            String foodIngredients = "";
            foodIngredients +=  ingredientsList.getByIndex(0).getName();

            for (int x=1;x<ingredientsList.getSize();x++)
            {
                foodIngredients += ","+ingredientsList.getByIndex(x).getName();
            }
            foodIngredientsTextView.setText(foodIngredients);
        }
        else
        {
            foodIngredientsTextView.setText(R.string.text_noIngredient);
        }
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
        FoodType newFoodType;
        switch (foodTypeSpinner.getSelectedItemPosition())
        {
            case 0:
                newFoodType = FoodType.Meat;
                break;
            case 1:
                newFoodType = FoodType.Vegetarian;
                break;
            case 2:
                newFoodType = FoodType.Other;
                break;
            default:
                newFoodType = FoodType.Other;
                break;
        }
        if (foodImage.getScaleType()==ImageView.ScaleType.FIT_CENTER)
        {
            return new Food(foodNameTextView.getText().toString(),newFoodType,ingredientsList);
        }
        return new Food(foodNameTextView.getText().toString(),newFoodType,ingredientsList,MyPicture.drawableToBitmap(foodImage.getDrawable()));
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}